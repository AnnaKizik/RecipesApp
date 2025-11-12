package com.example.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.databinding.FragmentListCategoriesBinding
import java.lang.IllegalStateException

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for FragmentListCategoriesBinding must not be null"
        )

    private lateinit var categoriesListViewModel: CategoriesListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        categoriesListViewModel = appContainer.categoriesListViewModelFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesListViewModel.loadCategoriesList()
        initUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi() {
        val categoriesAdapter = CategoriesListAdapter(emptyList())
        binding.rvCategories.adapter = categoriesAdapter

        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })

        categoriesListViewModel.categoriesListState.observe(viewLifecycleOwner) { state ->
            state.errorMessage?.let { error ->
                Toast.makeText(
                    context,
                    error,
                    Toast.LENGTH_SHORT
                ).show()
            }
            categoriesAdapter.updateData(state.categoriesList)
        }
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        categoriesListViewModel.categoriesListState.value?.errorMessage?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_SHORT
            ).show()
        }
        categoriesListViewModel.loadCategoryById(categoryId)
        categoriesListViewModel.categoriesListState.observe(viewLifecycleOwner) { state ->
            state.selectedCategory?.let { category ->
                findNavController().navigate(
                    CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                        category
                    )
                )
                categoriesListViewModel.clearSelectedCategory()
            }
        }
    }

}