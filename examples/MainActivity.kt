class MainActivity : LifecycleActivity() {
  //...
  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
  var mainViewModel: MainActivityModel? = null
  
  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this) // Dagger
    super.onCreate(savedInstanceState)
    //..
    mainViewModel = ViewModelProviders.of(this, viewModelFactory)
      .get(MainActivityModel::class.java)
  }
}