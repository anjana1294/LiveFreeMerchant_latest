package com.livefree.merchant.ui.profile

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.livefree.merchant.R
import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.RecyclerViewItemListener
import com.livefree.merchant.ui.profile.adapter.ProfileListAdpater
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : BaseFragment(), RecyclerViewItemListener {


    val items: ArrayList<String> = ArrayList()

    private var callback: Callback? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addProfileItem()
//        var i: Int;
        /*  for (x in 0..10) {
              items.add("Hii");
          }*/
        rv_profile.layoutManager = LinearLayoutManager(activity)
        rv_profile.adapter = ProfileListAdpater(items, context(), this)

        val dividerItemDecoration = DividerItemDecoration(
            rv_profile.getContext(),
            DividerItemDecoration.VERTICAL
        )
        rv_profile.addItemDecoration(dividerItemDecoration)

    }



    fun addProfileItem() {
        items.clear()
        items.add(resources.getString(R.string.about))
        items.add(resources.getString(R.string.businessDays))
        items.add(resources.getString(R.string.servers))
        items.add(resources.getString(R.string.sections))
        items.add(resources.getString(R.string.menu))
        items.add(resources.getString(R.string.payment))
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Callback)
            callback = context
    }

    override fun onItemClick(position: Int) {
        callback?.openProfileScreens(position)


    }

    public interface Callback {
        fun openProfileScreens(position: Int)
    }


    override fun onItemDelete(menuID: String) {

    }

    override fun onItemEdit(menuID: String) {
    }

}

