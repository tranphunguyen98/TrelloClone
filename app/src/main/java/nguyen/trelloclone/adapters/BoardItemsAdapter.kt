package nguyen.trelloclone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_board.view.*
import nguyen.trelloclone.R
import nguyen.trelloclone.models.Board

class BoardItemsAdapter(private val listBoard: List<Board>) : RecyclerView.Adapter<BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_board,parent,false)
        return BoardViewHolder(view)
    }

    override fun getItemCount(): Int = listBoard.size

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.bind(listBoard[position])
    }

}

class BoardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(board: Board){
        with(itemView) {
            Glide.with(context)
                .load(board.image)
                .placeholder(R.drawable.ic_board_place_holder)
                .into(iv_board_image)
            tv_name.text = board.name
            tv_created_by.text = board.createdBy
        }
    }
}