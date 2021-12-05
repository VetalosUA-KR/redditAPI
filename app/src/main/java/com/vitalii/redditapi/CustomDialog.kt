package com.vitalii.redditapi

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vitalii.redditapi.databinding.CustomDialogPhototBinding
import com.vitalii.redditapi.network.ImageLoader
import com.vitalii.redditapi.utils.Utils.Companion.DIALOG_PARAM_ARGS

class CustomDialog : DialogFragment() {

    private lateinit var binding: CustomDialogPhototBinding
    private lateinit var dialogView: View

    private var param: String? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initRootView()

        arguments?.let {
            param = it.getString(DIALOG_PARAM_ARGS)
        }

        val dialogBuilder = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.Theme_MaterialComponents_Dialog
        )
        onViewCreated(dialogView, savedInstanceState)
        dialogBuilder.setView(dialogView)
        return dialogBuilder.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(param)
    }

    private fun initUI(param: String?) {
        ImageLoader(binding.ivCustomDialogAvatar).execute(param)
    }

    private fun initRootView() {
        binding = CustomDialogPhototBinding.inflate(layoutInflater)
        dialogView = binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            CustomDialog().apply {
                arguments = Bundle().apply {
                    putString(DIALOG_PARAM_ARGS, param1)
                }
            }
    }
}