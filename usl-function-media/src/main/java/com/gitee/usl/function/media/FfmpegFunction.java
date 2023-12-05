package com.gitee.usl.function.media;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.annotation.Func;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.info.AudioInfo;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author hongda.li
 */
@Func
public class FfmpegFunction {

    @Func("ffmpeg.version")
    public String version() throws IOException {
        try (ProcessWrapper ffmpeg = new DefaultFFMPEGLocator().createExecutor()) {
            ffmpeg.addArgument("-version");
            ffmpeg.execute();
            return this.output(ffmpeg);
        }
    }

    @Func("ffmpeg.formats")
    public String formats() throws IOException {
        try (ProcessWrapper ffmpeg = new DefaultFFMPEGLocator().createExecutor()) {
            ffmpeg.addArgument("-formats");
            ffmpeg.execute();
            return this.output(ffmpeg);
        }
    }

    @Func("ffmpeg.codecs")
    public String codecs() throws IOException {
        try (ProcessWrapper ffmpeg = new DefaultFFMPEGLocator().createExecutor()) {
            ffmpeg.addArgument("-codecs");
            ffmpeg.execute();
            return this.output(ffmpeg);
        }
    }

    @Func("ffmpeg.devices")
    public String devices() throws IOException {
        try (ProcessWrapper ffmpeg = new DefaultFFMPEGLocator().createExecutor()) {
            ffmpeg.addArgument("-devices");
            ffmpeg.execute();
            return this.output(ffmpeg);
        }
    }

    @Func("ffmpeg.transform")
    public void transform(File source, File target) throws EncoderException {
        MultimediaObject multimediaObject = new MultimediaObject(source);

        // 获取音频文件的编码信息
        MultimediaInfo info = multimediaObject.getInfo();
        AudioInfo audioInfo = info.getAudio();

        // 设置音频属性
        AudioAttributes audio = new AudioAttributes();
        audio.setSamplingRate(audioInfo.getSamplingRate());
        audio.setChannels(audioInfo.getChannels());

        // 设置转码属性
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setInputFormat(FileUtil.getSuffix(source));
        attrs.setOutputFormat(FileUtil.getSuffix(target));
        attrs.setAudioAttributes(audio);

        // 音频转换格式类
        Encoder encoder = new Encoder();

        // 进行转换
        encoder.encode(multimediaObject, target, attrs);
    }

    private String output(ProcessWrapper ffmpeg) {
        String error = IoUtil.read(ffmpeg.getErrorStream(), StandardCharsets.UTF_8);
        if (CharSequenceUtil.isNotEmpty(error)) {
            return error;
        }
        return IoUtil.read(ffmpeg.getInputStream(), StandardCharsets.UTF_8);
    }
}
