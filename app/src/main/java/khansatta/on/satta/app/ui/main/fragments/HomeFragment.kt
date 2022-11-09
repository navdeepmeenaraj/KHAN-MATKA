package khansatta.on.satta.app.ui.main.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import khansatta.on.satta.app.R
import khansatta.on.satta.app.web_service.model.MarketData
import khansatta.on.satta.app.databinding.FragmentHomeBinding
import khansatta.on.satta.app.databinding.HeaderOneBinding
import khansatta.on.satta.app.ui.main.adapters.MarketsAdapter
import khansatta.on.satta.app.ui.main.view.HomeActivity
import khansatta.on.satta.app.ui.main.view.LoginActivity
import khansatta.on.satta.app.ui.main.view.PActivity
import khansatta.on.satta.app.ui.main.view.StarLineActivity
import khansatta.on.satta.app.ui.main.viewmodel.MainViewModel
import khansatta.on.satta.app.ui.main.viewmodel.SecondViewModel
import khansatta.on.satta.app.basic_utils.BasicUtils.bearerToken
import khansatta.on.satta.app.basic_utils.BasicUtils.checkIfUserIsVerified
import khansatta.on.satta.app.basic_utils.BasicUtils.cool
import khansatta.on.satta.app.basic_utils.BasicUtils.showErrorSnackBar
import khansatta.on.satta.app.basic_utils.Constants.PREFS_PHONE_NUMBER
import khansatta.on.satta.app.basic_utils.Constants.PREFS_WHATSAPP_NUMBER
import khansatta.on.satta.app.basic_utils.Status
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private val secondViewModel: SecondViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeContext: Context
    private lateinit var mView: View
    private lateinit var headerOneBinding: HeaderOneBinding

    private var marketList: List<MarketData> = listOf()
    private var marketsAdapter: MarketsAdapter? = null


    override fun onResume() {
        super.onResume()
        observeMarketData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        headerOneBinding = binding.headerOne
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        mainViewModel.bannerImageList()
        observeBannerImageList()
        observeMarketData()
        setOnClick()
        initPlayButton()
        getMainMarkets()
        (activity as HomeActivity?)?.getUserPoints()
    }

    private fun observeBannerImageList() {
        mainViewModel.imageList.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { images ->
                        val imageList = ArrayList<SlideModel>()
                        imageList.add(SlideModel(images.image_1))
                        imageList.add(SlideModel(images.image_2))
                        imageList.add(SlideModel(images.image_3))
                        val imageSlider = binding.imageSlider
                        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                }
            }
        }
    }


    private fun initPlayButton() {
        if (!checkIfUserIsVerified()) {
            binding.addPointsAppBar.visibility = View.GONE
            headerOneBinding.walletLayout.visibility = View.GONE
            headerOneBinding.otherGamesLayout.visibility = View.GONE
        }
    }


    private fun observeMarketData() {
        secondViewModel.market.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.mRefreshLayout.isRefreshing = false
                    it.data?.let { markets ->
                        initRecyclerView(markets)
                    }
                }
                Status.LOADING -> {
                    binding.mRefreshLayout.isRefreshing = true
                }
                Status.ERROR -> {
                    binding.mRefreshLayout.isRefreshing = false
                    requireActivity().showErrorSnackBar("Session Expired ! Login Again")
                }
            }
        }
    }

    private fun setOnClick() {

        headerOneBinding.buttonGali.setOnClickListener {
            Navigation.findNavController(it).navigate(
                R.id.galiMarketsFragment
            )
        }


        headerOneBinding.addPoints.setOnClickListener {
            startActivity(Intent(homeContext, PActivity::class.java))
        }

        headerOneBinding.getPoints.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.withdrawFragment)
        }


        val phoneNumber = Prefs.getString(PREFS_PHONE_NUMBER, "00")
        headerOneBinding.phoneNumber.text = phoneNumber.substring(3, 13)
        headerOneBinding.homeCallNow.setOnClickListener {

            try {
                if (phoneNumber != "00") {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("tel:$phoneNumber")
                    startActivity(intent)
                }
            } catch (e: Exception) {
                cool(e.message.toString())
            }

        }


        binding.addPointsAppBar.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.walletFragment)
        }

        val number = Prefs.getString(PREFS_WHATSAPP_NUMBER, "null")
        headerOneBinding.whatsappNumber.text = number.substring(3, 13)
        headerOneBinding.homeWhatsappNow.setOnClickListener {
            if (number != "null") {
                val url = "https://api.whatsapp.com/send?phone=$number"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }

        headerOneBinding.buttonStarline.setOnClickListener {
            startActivity(Intent(homeContext, StarLineActivity::class.java))
        }

        binding.mRefreshLayout.setOnRefreshListener {
            (activity as HomeActivity?)?.getUserPoints()
            getMainMarkets()
            initPlayButton()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeContext = context
    }

    private fun getMainMarkets() {
        val authToken = bearerToken()
        if (authToken != "null") {
            secondViewModel.fetchMarkets(authToken)
        } else {
            startActivity(Intent(homeContext, LoginActivity::class.java))
            activity?.finish()
        }
    }

    private fun initRecyclerView(markets: List<MarketData>) {
        marketList = markets
        binding.marketRecyclerView.layoutManager = LinearLayoutManager(homeContext)
        marketsAdapter = MarketsAdapter(marketList)
        binding.marketRecyclerView.adapter = marketsAdapter
        binding.marketRecyclerView.setItemViewCacheSize(markets.size)
        binding.marketRecyclerView.setHasFixedSize(true)
        marketsAdapter?.notifyDataSetChanged()
    }
}