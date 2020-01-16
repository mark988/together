package com.together.demo.service;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.LinearGradientColorPalette;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.awt.*;
import java.io.IOException;

@Slf4j
@Service
public class WordCloudServiceImpl implements WordCloudService {
    /**
     * @param wordFile 保存词汇文件
     * @return
     * @throws IOException
     */
    @Override
    public String  create(String wordFile,String destPngPath) throws IOException {
        //创建一个词语解析器,类似于分词
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(2);
        //这边要注意,引用了中文的解析器
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
        //拿到文档里面分出的词,和词频,建立一个集合存储起来
        java.util.List<WordFrequency> wordFrequencies =frequencyAnalyzer.load(wordFile);
        Dimension dimension = new Dimension(1920,1080);
        //设置图片相关的属性,这边是大小和形状,更多的形状属性,可以从CollisionMode源码里面查找
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        //这边要注意意思,是设置中文字体的,如果不设置,得到的将会是乱码,
        java.awt.Font font = new java.awt.Font("STSong-Light", 2, 16);
        wordCloud.setKumoFont(new KumoFont(font));
        wordCloud.setBackgroundColor(new Color(255, 255, 255));
        //因为我这边是生成一个圆形,这边设置圆的半径
        wordCloud.setBackground(new CircleBackground(255));
        //设置颜色
        wordCloud.setColorPalette(new LinearGradientColorPalette(Color.RED, Color.BLUE, Color.GREEN, 30, 30));
        wordCloud.setFontScalar(new SqrtFontScalar(12, 45));
        //将文字写入图片
        wordCloud.build(wordFrequencies);
        //生成图片
        wordCloud.writeToFile(destPngPath);
        return destPngPath;
    }
}
