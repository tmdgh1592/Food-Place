package com.app.service.parking.presentation.review.more

import com.app.service.parking.R
import com.app.service.parking.presentation.adapter.recyclerview.MoreReviewRVAdapter
import com.app.service.parking.databinding.FragmentBadReviewBinding
import com.app.service.parking.presentation.listener.RecyclerItemClickListener
import com.app.service.parking.presentation.base.BaseFragment
import com.app.service.parking.util.PopupImage
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BadReviewFragment : BaseFragment<FragmentBadReviewBinding, MoreReviewViewModel>() {

    override val layoutResId: Int = R.layout.fragment_bad_review
    override val viewModel: MoreReviewViewModel by sharedViewModel()
    private var moreReviewRVAdapter: MoreReviewRVAdapter? = null

    override fun initActivity() {
        binding.viewModel = viewModel

        with(binding) {
            // '더 보기 리뷰' 리사이클러뷰 어댑터 초기화
            moreReviewRVAdapter = MoreReviewRVAdapter(object: RecyclerItemClickListener {
                // 리뷰를 클릭했을 때
                override fun onClick(position: Int, resId: Int?) {
                    // 리뷰 이미지 Uri : 보안상 문제로 https를 http로 문자 변환
                    val imgUri = viewModel?.badReviewList?.value?.get(position)?.reviewImageUrl?.replace("https", "http")
                    // 이미지 팝업화면을 보여준다.
                    PopupImage().showImagePopup(requireContext(), imgUri)
                }
            })
            allReviewRecyclerView.adapter = moreReviewRVAdapter // 리사이클러뷰 어댑터 설정
            allReviewRecyclerView.layoutManager = MoreReviewRVAdapter.WrapContentLinearLayoutManager(requireContext())

            // 리뷰 데이터를 실시간 관찰
            viewModel?.badReviewList?.observe(this@BadReviewFragment) { reviewList ->
                // 리사이클러뷰 데이터 갱신
                moreReviewRVAdapter?.updateItems(reviewList)
            }

            // 서버로부터 리뷰 데이터 요청
            viewModel?.requestReviewList()
        }
    }
}