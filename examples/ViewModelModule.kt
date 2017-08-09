package com.tinmegali.daggerwithkotlin.dagger.viewModels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tinmegali.daggerwithkotlin.MainActivityModel
import com.tinmegali.oauth2restclient.dagger.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey( MainActivityModel::class )
    // Bind your View Model here
    abstract fun bindMainViewModel( mainViewModel: MainActivityModel ): ViewModel

    @Binds
    abstract fun bindViewModelFactory( factory: ViewModelFactory):
            ViewModelProvider.Factory

}