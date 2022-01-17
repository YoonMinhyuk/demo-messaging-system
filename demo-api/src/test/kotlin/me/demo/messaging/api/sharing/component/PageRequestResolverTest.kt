package me.demo.messaging.api.sharing.component

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import java.util.stream.Stream

internal class PageRequestResolverTest {

    @ParameterizedTest
    @MethodSource("testPageRequestCreateFromServerRequest")
    @DisplayName("ServerRequest 로 부터 PageRequest 를 성공적으로 생성해야한다.")
    internal fun testPageRequestCreateFromServerRequest(
        page: Int,
        size: Int,
        sorts: List<String>,
        expectedPage: Int,
        expectedSize: Int,
        expectedIsSort: Boolean
    ) {
        //given
        val serverRequestBuilder = MockServerRequest.builder()
            .queryParam("page", page.toString())
            .queryParam("size", size.toString())

        sorts.forEach { serverRequestBuilder.queryParam("sort", it) }

        val pageRequestResolver = createPageRequestResolver(
            defaultPageSize = DEFAULT_PAGE_SIZE,
            maxPageSize = MAX_PAGE_SIZE
        )

        //when
        val pageRequest = pageRequestResolver.resolve(serverRequestBuilder.build())

        //then
        assertThat(pageRequest.pageNumber).isEqualTo(expectedPage)
        assertThat(pageRequest.pageSize).isEqualTo(expectedSize)
        assertThat(pageRequest.sort.isSorted).isEqualTo(expectedIsSort)
        println(pageRequest)
    }

    private fun createPageRequestResolver(defaultPageSize: Int, maxPageSize: Int) =
        PageRequestResolver(defaultPageSize = defaultPageSize, maxPageSize = maxPageSize)

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
        private const val MAX_PAGE_SIZE = 100

        @JvmStatic
        fun testPageRequestCreateFromServerRequest(): Stream<Arguments> {
            val defaultPage = 0
            val defaultSize = 20
            val defaultSorts = listOf("id, desc", "createdAt, desc")

            return Stream.of(
                // page, size, sorts, expectedPage, expectedSize, expectedIsSort
                arguments(defaultPage, defaultSize, emptyList<String>(), defaultPage, defaultSize, false),
                arguments(defaultPage, defaultSize, defaultSorts, defaultPage, defaultSize, true),
                arguments(defaultPage, defaultSize, listOf("id, desc"), defaultPage, defaultSize, true),
                arguments(1, defaultSize, listOf("id, desc"), 1, defaultSize, true),
                arguments(1, DEFAULT_PAGE_SIZE, listOf("id, desc"), 1, DEFAULT_PAGE_SIZE, true),
                arguments(defaultPage, MAX_PAGE_SIZE, listOf("id, desc"), defaultPage, MAX_PAGE_SIZE, true),
                arguments(defaultPage, MAX_PAGE_SIZE + 1, listOf("id, desc"), defaultPage, DEFAULT_PAGE_SIZE, true),
                arguments(defaultPage, DEFAULT_PAGE_SIZE - 1, listOf("id, desc"), defaultPage, DEFAULT_PAGE_SIZE, true),
            )
        }
    }
}