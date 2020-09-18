package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.SeekBar;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.entity.LocalVoiceReverbPresetVo;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogSongVoiceRegulationBinding;
import com.deepsea.mua.stub.utils.SongStateUtils;

import java.util.List;

import static io.agora.rtc.Constants.AUDIO_REVERB_HIPHOP;
import static io.agora.rtc.Constants.AUDIO_REVERB_KTV;
import static io.agora.rtc.Constants.AUDIO_REVERB_OFF;
import static io.agora.rtc.Constants.AUDIO_REVERB_POPULAR;
import static io.agora.rtc.Constants.AUDIO_REVERB_RNB;
import static io.agora.rtc.Constants.AUDIO_REVERB_ROCK;
import static io.agora.rtc.Constants.AUDIO_REVERB_STUDIO;
import static io.agora.rtc.Constants.AUDIO_REVERB_VOCAL_CONCERT;

/**
 * Created by JUN on 2018/9/27
 */
public class SongVoiceRegulationDialog extends BaseDialog<DialogSongVoiceRegulationBinding> implements SeekBar.OnSeekBarChangeListener {

    public SongVoiceRegulationDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d("RegulationDialog",seekBar.getId()+";"+progress+";"+fromUser);
        if (seekBar.getId() == R.id.seekbar_reverb_dryevel || seekBar.getId() == R.id.seekbar_reverb_wetlevel) {
            String tag = (String) (findViewById(seekBar.getId())).getTag();
            if (mClickListener != null) {
                mClickListener.onVoiceLocalVoiceReverb(Integer.valueOf(tag), seekBar.getProgress() - 20);
            }
        } else if (seekBar.getId() == R.id.seekbar_reverb_roomsize || seekBar.getId() == R.id.seekbar_reverb_wetdelay || seekBar.getId() == R.id.seekbar_reverb_strength) {
            String tag = (String) (findViewById(seekBar.getId())).getTag();
            if (mClickListener != null) {
                mClickListener.onVoiceLocalVoiceReverb(Integer.valueOf(tag), seekBar.getProgress());
            }
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public interface OnClickListener {
        /**
         * 点击回调
         *
         * @param value
         */
        void onVoiceEffecClick(int value);

        void onVoicePitchClick(double value);

        void onVoiceLocalVoiceEqualization(int index, int value);

        void onVoiceLocalVoiceReverb(int index, int value);
    }

    private OnClickListener mClickListener;

    public OnClickListener getmClickListener() {
        return mClickListener;
    }

    public void setmClickListener(OnClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_song_voice_regulation;
    }

    @Override
    protected float getWidthPercent() {
        return 1;
    }

    @Override
    protected float getDimAmount() {
        return 0.2F;
    }

    public void init() {
        //关闭
        ViewBindUtils.RxClicks(mBinding.rlReverberationClose, o -> {
            dismiss();

        });        //音效
        ViewBindUtils.RxClicks(mBinding.tvSoundEffectPopular, o -> {
            setBtnSelect(AUDIO_REVERB_POPULAR);

        });
        ViewBindUtils.RxClicks(mBinding.tvSoundEffectRb, o -> {
            setBtnSelect(AUDIO_REVERB_RNB);
        });
        ViewBindUtils.RxClicks(mBinding.tvSoundEffectRock, o -> {
            setBtnSelect(AUDIO_REVERB_ROCK);
        });
        ViewBindUtils.RxClicks(mBinding.tvSoundEffectHiphop, o -> {
            setBtnSelect(AUDIO_REVERB_HIPHOP);

        });
        ViewBindUtils.RxClicks(mBinding.tvSoundEffectVocalconcer, o -> {
            setBtnSelect(AUDIO_REVERB_VOCAL_CONCERT);

        });
        ViewBindUtils.RxClicks(mBinding.tvSoundEffectKtv, o -> {
            setBtnSelect(AUDIO_REVERB_KTV);

        });
        ViewBindUtils.RxClicks(mBinding.tvSoundEffectStudio, o -> {
            setBtnSelect(AUDIO_REVERB_STUDIO);
        });
        mBinding.seekbarReverbRoomsize.setOnSeekBarChangeListener(this);
        mBinding.seekbarReverbWetdelay.setOnSeekBarChangeListener(this);
        mBinding.seekbarReverbStrength.setOnSeekBarChangeListener(this);
        mBinding.seekbarReverbWetlevel.setOnSeekBarChangeListener(this);
        mBinding.seekbarReverbDryevel.setOnSeekBarChangeListener(this);
        setSelectLocalReverb();
    }

    private void setSelectLocalReverb() {
        int localVoiceReverbPreset = SongStateUtils.getSingleton2().getSelectEffect();
        if (localVoiceReverbPreset == 1) {
            mBinding.tvSoundEffectPopular.setSelected(true);
        } else if (localVoiceReverbPreset == 2) {
            mBinding.tvSoundEffectRb.setSelected(true);
        } else if (localVoiceReverbPreset == 3) {
            mBinding.tvSoundEffectRock.setSelected(true);
        } else if (localVoiceReverbPreset == 4) {
            mBinding.tvSoundEffectHiphop.setSelected(true);
        } else if (localVoiceReverbPreset == 5) {
            mBinding.tvSoundEffectVocalconcer.setSelected(true);
        } else if (localVoiceReverbPreset == 6) {
            mBinding.tvSoundEffectKtv.setSelected(true);
        } else if (localVoiceReverbPreset == 7) {
            mBinding.tvSoundEffectStudio.setSelected(true);
        }
        List<LocalVoiceReverbPresetVo> voiceReverbPresetVoLis = SongStateUtils.getSingleton2().getReverbPresetVoList();
        if (voiceReverbPresetVoLis != null && voiceReverbPresetVoLis.size() > 0) {
            LocalVoiceReverbPresetVo vo = voiceReverbPresetVoLis.get(localVoiceReverbPreset - 1);

            mBinding.seekbarReverbDryevel.setProgress(20 + vo.getDryLevel());

            mBinding.seekbarReverbWetlevel.setProgress(20 + vo.getWetLevel());

            mBinding.seekbarReverbRoomsize.setProgress(vo.getRoomSize());

            mBinding.seekbarReverbWetdelay.setProgress(vo.getWetDelay());

            mBinding.seekbarReverbStrength.setProgress(vo.getStrength());

        }
    }

    private void setBtnSelect(int value) {
        String tagPopular = (String) mBinding.tvSoundEffectPopular.getTag();
        boolean isPopularSelect = tagPopular.equals(String.valueOf(value));
        if (isPopularSelect) {
            boolean isSelect = mBinding.tvSoundEffectPopular.isSelected();
            mBinding.tvSoundEffectPopular.setSelected(!isSelect);
        } else {
            mBinding.tvSoundEffectPopular.setSelected(false);
        }
        String tagRB = (String) mBinding.tvSoundEffectRb.getTag();
        boolean isRBSelect = tagRB.equals(String.valueOf(value));
        if (isRBSelect) {
            mBinding.tvSoundEffectRb.setSelected(!mBinding.tvSoundEffectRb.isSelected());
        } else {
            mBinding.tvSoundEffectRb.setSelected(false);
        }
        String tagRock = (String) mBinding.tvSoundEffectRock.getTag();
        boolean isRockSelect = tagRock.equals(String.valueOf(value));
        if (isRockSelect) {
            mBinding.tvSoundEffectRock.setSelected(!mBinding.tvSoundEffectRock.isSelected());
        } else {
            mBinding.tvSoundEffectRock.setSelected(false);

        }
        String tagHiphop = (String) mBinding.tvSoundEffectHiphop.getTag();
        boolean istagHiphopSelect = tagHiphop.equals(String.valueOf(value));
        if (istagHiphopSelect) {
            mBinding.tvSoundEffectHiphop.setSelected(!mBinding.tvSoundEffectHiphop.isSelected());
        } else {
            mBinding.tvSoundEffectHiphop.setSelected(false);

        }
        String tagVocalconcer = (String) mBinding.tvSoundEffectVocalconcer.getTag();
        boolean isVocalconerSelect = tagVocalconcer.equals(String.valueOf(value));
        if (isVocalconerSelect) {
            mBinding.tvSoundEffectVocalconcer.setSelected(!mBinding.tvSoundEffectVocalconcer.isSelected());
        } else {
            mBinding.tvSoundEffectVocalconcer.setSelected(false);

        }

        String tagKtv = (String) mBinding.tvSoundEffectKtv.getTag();
        boolean isKtvSelect = tagKtv.equals(String.valueOf(value));
        if (isKtvSelect) {
            mBinding.tvSoundEffectKtv.setSelected(!mBinding.tvSoundEffectKtv.isSelected());
        } else {
            mBinding.tvSoundEffectKtv.setSelected(false);

        }
        String tagStudio = (String) mBinding.tvSoundEffectStudio.getTag();
        boolean isStudioSelect = tagStudio.equals(String.valueOf(value));
        if (isStudioSelect) {
            mBinding.tvSoundEffectStudio.setSelected(!mBinding.tvSoundEffectStudio.isSelected());
        } else {
            mBinding.tvSoundEffectStudio.setSelected(false);

        }
        if (mClickListener != null) {
            boolean sect1 = mBinding.tvSoundEffectPopular.isSelected();
            boolean sect2 = mBinding.tvSoundEffectRb.isSelected();
            boolean sect3 = mBinding.tvSoundEffectHiphop.isSelected();
            boolean sect4 = mBinding.tvSoundEffectVocalconcer.isSelected();
            boolean sect5 = mBinding.tvSoundEffectKtv.isSelected();
            boolean sect6 = mBinding.tvSoundEffectStudio.isSelected();
            boolean sect7 = mBinding.tvSoundEffectRock.isSelected();
            boolean all = sect1 || sect2 || sect3 || sect4 || sect5 || sect6 || sect7;
            if (all) {
                SongStateUtils.getSingleton2().setSelectEffect(value);
                setSelectLocalReverb();
                mClickListener.onVoiceEffecClick(value);
            } else {
                mClickListener.onVoiceEffecClick(AUDIO_REVERB_OFF);
            }
        }
    }

}
