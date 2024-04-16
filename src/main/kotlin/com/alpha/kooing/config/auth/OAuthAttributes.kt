//package com.alpha.kooing.config.auth
//
//class OAuthAttributes(
//    val nameAttributesKey:String,
//    val attributes: MutableMap<String, Any>,
//    val email:String,
//    val name:String,
//){
//
//    companion object{
//        private fun ofGoogle(userAttrName:String, attributes: MutableMap<String, Any>):OAuthAttributes{
//            return OAuthAttributes(
//                userAttrName,
//                attributes,
//                attributes.get("email").toString(),
//                attributes.get("name").toString()
//            )
//        }
//
//        fun of(registrationId: String, attributes: MutableMap<String, Any>):OAuthAttributes?{
//            if(registrationId.equals("google")){
//                return ofGoogle(registrationId, attributes)
//            }else{
//                return null
//            }
//        }
//    }
//}