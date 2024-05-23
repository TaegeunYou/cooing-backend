data class JobPostingDto(
    val resultCode: String? = null,
    val resultMsg: String? = null,
    val totalCount: Int? = null,
    val result: List<RecruitmentResult>? = null
) {
    data class RecruitmentResult(
        val recrutPblntSn: String? = null,
        val pblntInstCd: String? = null,
        val pbadmsStdInstCd: String? = null,
        val instNm: String? = null,
        val ncsCdLst: String? = null,
        val ncsCdNmLst: String? = null,
        val hireTypeLst: String? = null,
        val hireTypeNmLst: String? = null,
        val workRgnLst: String? = null,
        val workRgnNmLst: String? = null,
        val recrutSe: String? = null,
        val recrutSeNm: String? = null,
        val prefCondCn: String? = null,
        val recrutNope: String? = null,
        val pbancBgngYmd: String? = null,
        val pbancEndYmd: String? = null,
        val recrutPbancTtl: String? = null,
        val srcUrl: String? = null,
        val replmprYn: String? = null,
        val aplyQlfcCn: String? = null,
        val disqlfcRsn: String? = null,
        val scrnprcdrMthdExpln: String? = null,
        val prefCn: String? = null,
        val acbgCondLst: String? = null,
        val acbgCondNmLst: String? = null,
        val nonatchRsn: String? = null,
        val ongoingYn: String? = null,
        val decimalDay: String? = null
    )
}