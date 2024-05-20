package com.alpha.kooing.board.service

import com.alpha.kooing.board.dto.*
import com.alpha.kooing.board.entity.Club
import com.alpha.kooing.board.entity.Study
import com.alpha.kooing.board.entity.Volunteer
import com.alpha.kooing.board.repository.ClubRepository
import com.alpha.kooing.board.repository.StudyRepository
import com.alpha.kooing.board.repository.VolunteerRepository
import com.alpha.kooing.config.jwt.JwtTokenProvider
import com.alpha.kooing.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.jvm.optionals.getOrNull

@Service
class CollegeService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val volunteerRepository: VolunteerRepository,
    private val clubRepository: ClubRepository,
    private val studyRepository: StudyRepository,
    private val userRepository: UserRepository
) {

    @Transactional(readOnly = true)
    fun getVolunteers(token: String, query: String?): List<VolunteerSummary> {
        val volunteers = if (query == null) {
            volunteerRepository.findAll()
        } else {
            volunteerRepository.findAllByTitleOrSummaryOrContentContaining(query)
        }
        return volunteers.map { volunteer ->
            VolunteerSummary(
                volunteer.id!!,
                volunteer.title,
                volunteer.summary,
                volunteer.imageUrl
            )
        }.sortedByDescending { it.volunteerId }
    }

    @Transactional(readOnly = true)
    fun getVolunteer(volunteerId: Long): VolunteerDetail {
        val volunteer = volunteerRepository.findById(volunteerId).getOrNull() ?: throw Exception("해당 봉사활동을 찾을 수 없습니다.")
        return VolunteerDetail(
            volunteer.title,
            volunteer.summary,
            volunteer.imageUrl,
            volunteer.recruitStartDate.toString(),
            volunteer.recruitEndDate.toString(),
            volunteer.content,
            volunteer.user.id!!
        )
    }

    @Transactional
    fun createVolunteer(token: String, request: CreateVolunteerRequest) {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        volunteerRepository.save(
            Volunteer(
                null,
                user,
                request.title,
                request.summary,
                request.imageUrl,
                LocalDate.parse(request.recruitStartDate, DateTimeFormatter.ISO_DATE),
                LocalDate.parse(request.recruitEndDate, DateTimeFormatter.ISO_DATE),
                request.content
            )
        )
    }

    @Transactional(readOnly = true)
    fun getClubs(token: String, query: String?): List<ClubSummary> {
        val clubs = if (query == null) {
            clubRepository.findAll()
        } else {
            clubRepository.findAllByTitleOrSummaryOrContentContaining(query)
        }
        return clubs.map { club ->
            ClubSummary(
                club.id!!,
                club.title,
                club.summary,
                club.imageUrl
            )
        }.sortedByDescending { it.clubId }
    }

    @Transactional(readOnly = true)
    fun getClub(clubId: Long): ClubDetail {
        val club = clubRepository.findById(clubId).getOrNull() ?: throw Exception("해당 동아리 및 소모임을 찾을 수 없습니다.")
        return ClubDetail(
            club.title,
            club.summary,
            club.imageUrl,
            club.recruitStartDate.toString(),
            club.recruitEndDate.toString(),
            club.content,
            club.user.id!!
        )
    }

    @Transactional
    fun createClub(token: String, request: CreateClubRequest) {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        clubRepository.save(
            Club(
                null,
                user,
                request.title,
                request.summary,
                request.imageUrl,
                LocalDate.parse(request.recruitStartDate, DateTimeFormatter.ISO_DATE),
                LocalDate.parse(request.recruitEndDate, DateTimeFormatter.ISO_DATE),
                request.content
            )
        )
    }

    @Transactional(readOnly = true)
    fun getStudies(token: String, query: String?): List<StudySummary>? {
        val studies = if (query == null) {
            studyRepository.findAll()
        } else {
            studyRepository.findAllByTitleOrContentContaining(query)
        }
        return studies.map { study ->
            StudySummary(
                study.id!!,
                study.title,
                study.category,
                study.location,
                study.createDatetime.toString()
            )
        }.sortedByDescending { it.studyId }
    }

    @Transactional(readOnly = true)
    fun getStudy(studyId: Long): StudyDetail {
        val study = studyRepository.findById(studyId).getOrNull() ?: throw Exception("해당 스터디를 찾을 수 없습니다.")
        return StudyDetail(
            study.title,
            study.category,
            study.location,
            study.numberOfMembers,
            study.content
        )
    }

    @Transactional
    fun createStudy(token: String, request: CreateStudyRequest) {
        val userEmail = jwtTokenProvider.getJwtEmail(token)
        val user = userRepository.findByEmail(userEmail).getOrNull() ?: throw Exception("로그인 유저 정보가 올바르지 않습니다.")
        studyRepository.save(
            Study(
                null,
                user,
                request.title,
                request.category,
                request.location,
                request.numberOfMembers,
                request.content,
                LocalDateTime.now()
            )
        )
    }

}