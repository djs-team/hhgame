package com.android.beauty.transmit;

import com.android.beauty.capture.VideoCaptureFrame;
import com.android.beauty.connector.SinkConnector;


import io.agora.rtc.mediaio.MediaIO;

public class VideoTransmitter implements SinkConnector<VideoCaptureFrame> {
    private VideoSource mSource;

    public VideoTransmitter(VideoSource source) {
        mSource = source;
    }

    public int onDataAvailable(VideoCaptureFrame data) {
        if (mSource.getConsumer() != null) {
            boolean needsFixWidthAndHeight = data.mRotation == 90 || data.mRotation == 270;
            mSource.getConsumer().consumeTextureFrame(data.mTextureId,
                    MediaIO.PixelFormat.TEXTURE_2D.intValue(), needsFixWidthAndHeight ? data.mFormat.getHeight() : data.mFormat.getWidth(),
                    needsFixWidthAndHeight ? data.mFormat.getWidth() : data.mFormat.getHeight(), 0, data.mTimeStamp, data.mTexMatrix);
        }
        return 0;
    }
}
