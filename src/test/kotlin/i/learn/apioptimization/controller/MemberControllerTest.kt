package i.learn.apioptimization.controller

import i.learn.apioptimization.InitDb
import i.learn.apioptimization.controller.interfaces.*
import i.learn.apioptimization.domain.Member
import i.learn.apioptimization.exception.RestExceptionView
import i.learn.apioptimization.repository.MemberRepository
import i.learn.apioptimization.service.MessageSourceService
import i.learn.apioptimization.support.MessageKey
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberControllerTest(
    @Autowired val restTemplate: TestRestTemplate
){
    @Autowired
    lateinit var messageSourceService: MessageSourceService

    @Autowired
    lateinit var memberRepository: MemberRepository

    @MockBean
    lateinit var initDb: InitDb

    @BeforeAll
    fun beforeAll() {
        memberRepository.deleteAll()
    }

    @Test
    fun create() {
        val request = CreateMemberRequest(name = "alice")
        val response = restTemplate.exchange<CreatedMemberResponse>(
            "/members",
            HttpMethod.POST,
            HttpEntity(request)
        )

        assertEquals(response.statusCode, HttpStatus.CREATED)
        val createdMember = response.body
        assertNotNull(createdMember?.id)
        assertEquals(request.name, createdMember?.name)
    }

    @Test
    fun update() {
        val member = memberRepository.save(Member(name = "alice"))
        assertNotNull(member.id)

        val request = UpdateMemberRequest(name = "bob")
        val response = restTemplate.exchange<UpdatedMemberResponse>(
            "/members/${member.id}",
            HttpMethod.PUT,
            HttpEntity(request)
        )

        assertEquals(response.statusCode, HttpStatus.OK)
        val updatedMember = response.body
        assertEquals(member.id, updatedMember?.id)
        assertEquals(request.name, updatedMember?.name)
    }

    @Test
    fun notFoundException() {
        val unknownId = Integer.MIN_VALUE
        val response = restTemplate.exchange<RestExceptionView>(
            "/members/${unknownId}",
            HttpMethod.PUT,
            HttpEntity(UpdateMemberRequest(name = "bob"))
        )

        assertEquals(response.statusCode, HttpStatus.NOT_FOUND)
        assertEquals(response.body?.message, messageSourceService.getMessage(MessageKey.NOT_FOUND_EXCEPTION))
    }

    @Test
    fun get() {
        val members = memberRepository.saveAll(listOf(
            Member(name = "alice"),
            Member(name = "bob")
        ))

        val response = restTemplate.exchange<WrappedResponse<List<GetMemberResponse>>>(
            "/members",
            HttpMethod.GET,
            HttpEntity.EMPTY
        )

        assertEquals(response.statusCode, HttpStatus.OK)
        assertNotNull(response.body?.data)
        val data = response.body!!.data
        assertTrue(members.size <= data.size)
        assertNotNull(data.find { it.name == members[0].name })
        assertNotNull(data.find { it.name == members[1].name })
    }
}