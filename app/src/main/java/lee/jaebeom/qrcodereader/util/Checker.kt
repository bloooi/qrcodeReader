package lee.jaebeom.qrcodereader.util

import java.util.regex.Pattern

/**
 * Created by leejaebeom on 2018. 2. 18..
 */
class Checker {
    companion object {
        fun checkData(data: String) : String{
            val urlPattern = Pattern.compile("^(http|https)://(.*)")

            return  if (urlPattern.matcher(data).matches()){
                "URL"
            }else{
                "plain"
            }
        }
    }
}