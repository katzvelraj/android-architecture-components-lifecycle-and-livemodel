package com.tinmegali.daggerwithkotlin.dagger.viewModels

import com.tinmegali.daggerwithkotlin.MainActivityModel
import dagger.Component

@Component( modules = arrayOf(
        ViewModelModule::class
))
interface ViewModelComponent {
    // inject your view models
    fun inject( mainViewModel: MainActivityModel )

}