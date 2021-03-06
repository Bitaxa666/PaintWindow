package edu.example.paintwindow;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ColorDialogFragment extends DialogFragment 
{
	private SeekBar alphaSeekBar;
	private SeekBar redSeekBar;
	private SeekBar greenSeekBar;
	private SeekBar blueSeekBar;
	private View colorView;
	private int color;
	//create AlertDialog for Set Color and return it
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View colorDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_color, null);
		builder.setView(colorDialogView);
		builder.setTitle(R.string.title_color_dialog);
		builder.setCancelable(true);
		
		alphaSeekBar = (SeekBar) colorDialogView.findViewById(R.id.alphaSeekBar);
		redSeekBar = (SeekBar) colorDialogView.findViewById(R.id.redSeekBar);
		greenSeekBar = (SeekBar) colorDialogView.findViewById(R.id.greenSeekBar);
		blueSeekBar = (SeekBar) colorDialogView.findViewById(R.id.blueSeekBar);
		
		alphaSeekBar.setOnSeekBarChangeListener(colorChangedListener);
		redSeekBar.setOnSeekBarChangeListener(colorChangedListener);
		greenSeekBar.setOnSeekBarChangeListener(colorChangedListener);
		blueSeekBar.setOnSeekBarChangeListener(colorChangedListener);
		
		final DoodleView doodleView = getDoodleFragment().getDoodleView();
		color = doodleView.getDrawingColor();
		
		alphaSeekBar.setProgress(Color.alpha(color));
		redSeekBar.setProgress(Color.red(color));
		greenSeekBar.setProgress(Color.green(color));
		blueSeekBar.setProgress(Color.blue(color));
	
		
		builder.setPositiveButton(R.string.button_set_color, new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{				
				doodleView.setDrawingColor(color);
			}
		});
		return builder.create();
	}
	//create new fragment (doodleFragment)
	private DoodleFragment getDoodleFragment()
	{
		return (DoodleFragment) getFragmentManager().findFragmentById(R.id.doodleFragment);
	}
	@Override
	public void onAttach(Activity activity) 
	{		
		super.onAttach(activity);
		DoodleFragment fragment = getDoodleFragment();
		if(fragment!=null)
		{
			fragment.setDialogOnScreen(true);
		}
	}
	@Override
	public void onDetach()
	{		
		super.onDetach();
		DoodleFragment fragment = getDoodleFragment();
		if(fragment!=null)
		{
			fragment.setDialogOnScreen(false);
		}
		
	}
	private OnSeekBarChangeListener colorChangedListener = new OnSeekBarChangeListener() 
	{
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar)
		{				
			
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) 
		{		
			
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser)
		{
			if(fromUser)
				color=Color.argb(alphaSeekBar.getProgress(),
									redSeekBar.getProgress(),
									greenSeekBar.getProgress(),
									blueSeekBar.getProgress());			
			
			}
	};

}
