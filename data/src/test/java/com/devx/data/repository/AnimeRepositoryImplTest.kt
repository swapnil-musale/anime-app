package com.devx.data.repository

import com.devx.data.core.FakeConnectivityManager
import com.devx.data.local.entity.AnimeDetailEntity
import com.devx.data.local.entity.AnimeEntity
import com.devx.data.localSource.FakeAnimeLocalDataSource
import com.devx.data.remote.dto.animeDetail.AnimeDetailDto
import com.devx.data.remote.dto.animeDetail.AnimeDetailResponse
import com.devx.data.remote.dto.animeList.AnimeDto
import com.devx.data.remote.dto.animeList.AnimeListResponse
import com.devx.data.remote.dto.animeList.PaginationDto
import com.devx.data.remote.util.NetworkResult
import com.devx.data.remoteSource.FakeAnimeRemoteDataSource
import com.devx.domain.core.ConnectivityManager
import com.devx.domain.model.PaginationInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AnimeRepositoryImplTest {

    private lateinit var fakeRemoteDataSource: FakeAnimeRemoteDataSource
    private lateinit var fakeLocalDataSource: FakeAnimeLocalDataSource
    private lateinit var fakeConnectivityManager: FakeConnectivityManager
    private lateinit var repository: AnimeRepositoryImpl

    @Before
    fun setUp() {
        fakeRemoteDataSource = FakeAnimeRemoteDataSource()
        fakeLocalDataSource = FakeAnimeLocalDataSource()
        fakeConnectivityManager = FakeConnectivityManager()
        repository = AnimeRepositoryImpl(
            remoteDataSource = fakeRemoteDataSource,
            localDataSource = fakeLocalDataSource,
            connectivityManager = fakeConnectivityManager,
        )
    }

    @Test
    fun `fetchAnimePage online network success stores data and returns pagination info`() =
        runTest(UnconfinedTestDispatcher()) {
            fakeConnectivityManager.setStatus(ConnectivityManager.Status.AVAILABLE)
            fakeRemoteDataSource.topAnimeResult = NetworkResult.Success(
                AnimeListResponse(
                    pagination = PaginationDto(hasNextPage = true, currentPage = 1),
                    animeList = listOf(
                        AnimeDto(malId = 1, title = "Anime title 1"),
                        AnimeDto(malId = 2, title = "Anime title 2"),
                    ),
                )
            )

            val result = repository.fetchAnimePage(isRefresh = true)

            assertTrue(result.isSuccess)
            assertEquals(PaginationInfo(hasNextPage = true), result.getOrThrow())
            assertEquals(2, fakeLocalDataSource.replacedList.size)
            assertEquals(1, fakeLocalDataSource.replacedList[0].malId)
            assertEquals(2, fakeLocalDataSource.replacedList[1].malId)
        }

    @Test
    fun `fetchAnimePage offline with no cached data returns failure`() =
        runTest(UnconfinedTestDispatcher()) {
            fakeConnectivityManager.setStatus(ConnectivityManager.Status.UNAVAILABLE)
            fakeLocalDataSource.storedList = emptyList()

            val result = repository.fetchAnimePage(isRefresh = false)

            assertTrue(result.isFailure)
            assertEquals("No internet connection", result.exceptionOrNull()?.message)
        }

    @Test
    fun `fetchAnimePage offline with cached data returns success without network call`() =
        runTest(UnconfinedTestDispatcher()) {
            fakeConnectivityManager.setStatus(ConnectivityManager.Status.UNAVAILABLE)
            fakeLocalDataSource.storedList = listOf(getFakeAnimeEntity(malId = 1))

            val result = repository.fetchAnimePage(isRefresh = false)

            assertTrue(result.isSuccess)
            assertEquals(PaginationInfo(hasNextPage = false), result.getOrThrow())
            assertTrue(fakeRemoteDataSource.fetchTopAnimeCalled.not())
        }

    @Test
    fun `fetchAnimeDetail online network success stores data and returns anime detail`() =
        runTest(UnconfinedTestDispatcher()) {
            fakeConnectivityManager.setStatus(ConnectivityManager.Status.AVAILABLE)
            fakeLocalDataSource.cachedDetail = null

            fakeRemoteDataSource.animeDetailResult = NetworkResult.Success(
                AnimeDetailResponse(
                    animeDetail = AnimeDetailDto(
                        malId = 1,
                        title = "Fullmetal Alchemist: Brotherhood"
                    ),
                )
            )

            val result = repository.fetchAnimeDetail(animeId = 21)

            assertTrue(result.isSuccess)
            assertEquals(1, result.getOrThrow().malId)
            assertEquals("Fullmetal Alchemist: Brotherhood", result.getOrThrow().title)
            assertNotNull(fakeLocalDataSource.insertedDetail)
            assertEquals(1, fakeLocalDataSource.insertedDetail?.malId)
        }

    @Test
    fun `fetchAnimeDetail offline with no cached data returns failure`() =
        runTest(UnconfinedTestDispatcher()) {
            fakeConnectivityManager.setStatus(ConnectivityManager.Status.UNAVAILABLE)
            fakeLocalDataSource.cachedDetail = null

            val result = repository.fetchAnimeDetail(animeId = 99)

            assertTrue(result.isFailure)
            assertEquals(
                "No internet connection and no cached data",
                result.exceptionOrNull()?.message,
            )
        }

    @Test
    fun `fetchAnimeDetail offline with cached data returns cached anime detail`() =
        runTest(UnconfinedTestDispatcher()) {
            val cachedEntity = getFakeAnimeDetailEntity(malId = 5)
            fakeConnectivityManager.setStatus(ConnectivityManager.Status.UNAVAILABLE)
            fakeLocalDataSource.cachedDetail = cachedEntity

            val result = repository.fetchAnimeDetail(animeId = 5)

            assertTrue(result.isSuccess)
            assertEquals(5, result.getOrThrow().malId)
            assertTrue(fakeRemoteDataSource.fetchAnimeDetailCalled.not())
        }
}

fun getFakeAnimeEntity(malId: Int = 1): AnimeEntity = AnimeEntity(
    malId = malId,
    title = "Anime #$malId",
    titleEnglish = "Anime #$malId",
    episodes = 24,
    score = 8.5,
    imageUrl = "",
    synopsis = "Synopsis for anime $malId",
)

fun getFakeAnimeDetailEntity(malId: Int = 1): AnimeDetailEntity = AnimeDetailEntity(
    malId = malId,
    title = "Anime #$malId",
    titleEnglish = "Anime #$malId",
    episodes = 24,
    score = 8.5,
    imageUrl = "",
    synopsis = "Synopsis for anime $malId",
    genres = "Action,Adventure",
    rating = "PG-13",
    trailerEmbedUrl = null,
)
