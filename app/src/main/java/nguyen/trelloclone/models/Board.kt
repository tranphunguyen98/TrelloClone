package nguyen.trelloclone.models

data class Board(
    val name: String = "",
    val image: String = "",
    val createdBy: String = "",
    val assignedTo: ArrayList<String> = ArrayList(),
    var id: String = ""

    )
//    : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString()!!,
//        parcel.readString()!!,
//        parcel.readString()!!,
//        parcel.createStringArrayList()!!
//    ) {
//    }
//
//    companion object CREATOR : Parcelable.Creator<Board> {
//        override fun createFromParcel(parcel: Parcel): Board {
//            return Board(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Board?> {
//            return arrayOfNulls(size)
//        }
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(name)
//        parcel.writeString(image)
//        parcel.writeString(createdBy)
//        parcel.writeStringList(assignedTo)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//}