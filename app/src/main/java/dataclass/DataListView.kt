package dataclass

class DataListView(name:String,image:String) {
    private val name:String
    private val image:String

    init {
        this.name = name
        this.image = image
    }
}