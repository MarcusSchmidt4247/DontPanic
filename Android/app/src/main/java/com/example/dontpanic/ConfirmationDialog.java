// MS: 3/18/22 - created dialog based on https://developer.android.com/guide/topics/ui/dialogs.html

package com.example.dontpanic;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ConfirmationDialog extends DialogFragment
{
    public interface ConfirmationDialogListener
    {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    public static final String MESSAGE_KEY = "dialogMessage";
    public static final String EXTRA_INT_KEY = "extraInt";

    private static final String MESSAGE_DEFAULT = "Are you sure?";
    private ConfirmationDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        String message;
        if (getArguments() != null)
            message = getArguments().getString(MESSAGE_KEY, MESSAGE_DEFAULT);
        else
            message = MESSAGE_DEFAULT;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(message)
                .setPositiveButton(R.string.yesButton, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        listener.onDialogPositiveClick(ConfirmationDialog.this);
                    }
                })
                .setNegativeButton(R.string.noButton, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        listener.onDialogNegativeClick(ConfirmationDialog.this);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        try
        {
            listener = (ConfirmationDialogListener) context;
        }
        catch (ClassCastException e)
        {
            e.printStackTrace();
        }
    }

    // Source: https://stackoverflow.com/questions/9245408
    public static ConfirmationDialog newInstance(@Nullable String message, @Nullable Integer extraInt)
    {
        ConfirmationDialog dialog = new ConfirmationDialog();

        Bundle args = new Bundle();
        args.putString(MESSAGE_KEY, message);
        if (extraInt != null)
            args.putInt(EXTRA_INT_KEY, extraInt);
        dialog.setArguments(args);

        return dialog;
    }
}
