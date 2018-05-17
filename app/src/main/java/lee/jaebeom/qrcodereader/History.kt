package lee.jaebeom.qrcodereader

/**
 * Created by leejaebeom on 2018. 2. 16..
 */
data class History(val name: String, val content: String, val time: String){
    override fun equals(other: Any?): Boolean {
        if (other is History){
            return other.content == this.content
        }
        return false
    }
}