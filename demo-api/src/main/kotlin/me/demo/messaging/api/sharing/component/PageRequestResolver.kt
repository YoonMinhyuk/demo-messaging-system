package me.demo.messaging.api.sharing.component

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.queryParamOrNull

@Component
final class PageRequestResolver(
    @Value("\${spring.data.web.pageable.one-indexed-parameters:false}")
    val isOneIndexedParameters: Boolean = false,

    @Value("\${spring.data.web.pageable.default-page-size:20}")
    val defaultPageSize: Int = 20,

    @Value("\${spring.data.web.pageable.max-page-size:100}")
    val maxPageSize: Int = 500,

    @Value("\${spring.data.web.pageable.page-parameter:page}")
    val pageParameterName: String = "page",

    @Value("\${spring.data.web.pageable.size-parameter:size}")
    val sizeParameterName: String = "size",

    @Value("\${spring.data.web.pageable.prefix:''}")
    val prefix: String = "",

    @Value("\${spring.data.web.pageable.qualifier-delimiter:_}")
    val qualifierDelimiter: String = "_",
) {
    private val firstPageNumber: Int = if (isOneIndexedParameters) 1 else 0

    fun resolve(serverRequest: ServerRequest): PageRequest =
        PageRequest.of(getPage(serverRequest), getPageSize(serverRequest), getSort(serverRequest))

    private fun getPage(serverRequest: ServerRequest): Int {
        val page = serverRequest.queryParamOrNull(pageParameterName)?.toInt() ?: return firstPageNumber
        if (page < firstPageNumber) {
            return firstPageNumber
        }
        return page
    }

    private fun getPageSize(serverRequest: ServerRequest): Int {
        val pageSize = serverRequest.queryParamOrNull(sizeParameterName)?.toInt() ?: return defaultPageSize
        if (pageSize < defaultPageSize) return defaultPageSize
        if (pageSize > maxPageSize) return defaultPageSize
        return pageSize
    }

    private fun getSort(serverRequest: ServerRequest): Sort =
        Sort.by(
            serverRequest.queryParams()
                .filter { entry -> entry.key.equals("sort", true) }
                .values
                .flatten()
                .asSequence()
                .filter { it != null && it.isNotEmpty() && it.isNotEmpty() }
                .map {
                    if (!it.contains(",")) return@map Sort.Order.asc(it)
                    val split = it.split(",")
                    if (split[1] === "asc") return@map Sort.Order.asc(split[0])
                    Sort.Order.desc(split[0])
                }
                .toMutableList()
        )
}