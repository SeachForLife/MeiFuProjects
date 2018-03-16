package com.carl_yang.domain;

import com.carl_yang.art.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by carl_yang on 2017/5/3.
 */

public class courseWareProperty {

    private static courseWareProperty mInstance = new courseWareProperty();

    public static courseWareProperty getmInstance() {
        return mInstance;
    }

    private Integer[] mCoverImageIds = {
//            R.mipmap.cover_1, R.drawable.cover_2, R.drawable.cover_3, R.drawable.cover_4,
//            R.mipmap.cover_5, R.drawable.cover_6, R.drawable.cover_7, R.drawable.cover_8,
//            R.mipmap.cover_5, R.drawable.cover_6, R.drawable.cover_7, R.drawable.cover_8
    };

    private Map<Integer,Integer> mCoverIdMap=new HashMap<Integer,Integer>(){{
        put(1,R.mipmap.cover_1);put(2, R.mipmap.cover_2);
        put(3, R.mipmap.cover_3);put(4, R.mipmap.cover_4);
        put(5, R.mipmap.cover_5);put(6, R.mipmap.cover_6);
        put(7, R.mipmap.cover_7);put(8, R.mipmap.cover_8);
        put(9, R.mipmap.cover_9);put(10, R.mipmap.cover_10);
        put(11, R.mipmap.cover_11);put(12, R.mipmap.cover_12);
        put(13, R.mipmap.cover_13);put(14, R.mipmap.cover_14);
        put(15, R.mipmap.cover_15);put(16, R.mipmap.cover_16);
        put(17, R.mipmap.cover_17);put(18, R.mipmap.cover_18);
    }};

    private Map<Integer,String> mCoverNameMap=new HashMap<Integer,String>(){{
        put(1,"一年级（上册）");put(2,"一年级（下册）");
        put(3,"二年级（上册）");put(4,"二年级（下册）");
        put(5,"三年级（上册）");put(6,"三年级（下册）");
        put(7,"四年级（上册）");put(8,"四年级（下册）");
        put(9,"五年级（上册）");put(10,"五年级（下册）");
        put(11,"六年级（上册）");put(12,"六年级（下册）");
        put(13,"七年级（上册）");put(14,"七年级（下册）");
        put(15,"八年级（上册）");put(16,"八年级（下册）");
        put(17,"九年级（上册）");put(18,"九年级（下册）");
    }};


    private String[] mCoverStringNames = {
//            "一年级（上册）", "一年级（下册）", "二年级（上册）", "二年级（下册）",
//            "三年级（上册）", "三年级（下册）", "四年级（上册）", "四年级（下册）",
//            "五年级（上册）", "五年级（下册）", "六年级（上册）", "六年级（下册）"
    };

    public String[] getmCoverStringNames() {
        return mCoverStringNames;
    }

    public void setmCoverStringNames(String[] mCoverStringNames) {
        this.mCoverStringNames = mCoverStringNames;
    }

    public Integer[] getmCoverImageIds() {
        return mCoverImageIds;
    }

    public void setmCoverImageIds(Integer[] mCoverImageIds) {
        this.mCoverImageIds = mCoverImageIds;
    }

    public Map<Integer, Integer> getmCoverIdMap() {
        return mCoverIdMap;
    }

    public void setmCoverIdMap(Map<Integer, Integer> mCoverIdMap) {
        this.mCoverIdMap = mCoverIdMap;
    }

    public Map<Integer, String> getmCoverNameMap() {
        return mCoverNameMap;
    }

    public void setmCoverNameMap(Map<Integer, String> mCoverNameMap) {
        this.mCoverNameMap = mCoverNameMap;
    }

    private Integer[] mClassroomImageIds1_1 = {
            R.mipmap.firstgrade_class_1, R.mipmap.firstgrade_class_2, R.mipmap.firstgrade_class_3, R.mipmap.firstgrade_class_4,R.mipmap.firstgrade_class_5,
            R.mipmap.firstgrade_class_6, R.mipmap.firstgrade_class_7, R.mipmap.firstgrade_class_8, R.mipmap.firstgrade_class_9,R.mipmap.firstgrade_class_10,
            R.mipmap.firstgrade_class_11, R.mipmap.firstgrade_class_12, R.mipmap.firstgrade_class_13, R.mipmap.firstgrade_class_14,R.mipmap.firstgrade_class_15,
            R.mipmap.firstgrade_class_16, R.mipmap.firstgrade_class_17, R.mipmap.firstgrade_class_18, R.mipmap.firstgrade_class_19
    };

    private String[] mClassroomStringNames1_1 = {
            "1、涂涂画画", "2、图形变变变", "3、可爱的家", "4、找妈妈","5、玩泥巴",
            "6、多样的小饼干", "7、看谁摆的花样多", "8、变脸的太阳.", "9、我的手","10、勤劳的蚂蚁",
            "11、昆虫一家", "12、多彩的秋天", "13、美丽的大自然", "14、早餐","15、特色小吃",
            "16、小彩灯", "17、花手套", "18、偶戏", "19、民间泥玩具"
    };

    private Integer[] mClassroomImageIds1_2 = {
    };

    private String[] mClassroomStringNames1_2 = {
    };

    private Integer[] mClassroomImageIds2_1 = {
            R.mipmap.secondgrade_upclass_1, R.mipmap.secondgrade_upclass_2, R.mipmap.secondgrade_upclass_3,R.mipmap.secondgrade_upclass_4, R.mipmap.secondgrade_upclass_5,
            R.mipmap.secondgrade_upclass_6, R.mipmap.secondgrade_upclass_7, R.mipmap.secondgrade_upclass_8,R.mipmap.secondgrade_upclass_9, R.mipmap.secondgrade_upclass_10,
            R.mipmap.secondgrade_upclass_11, R.mipmap.secondgrade_upclass_12, R.mipmap.secondgrade_upclass_13,R.mipmap.secondgrade_upclass_14, R.mipmap.secondgrade_upclass_15,
            R.mipmap.secondgrade_upclass_16, R.mipmap.secondgrade_upclass_17, R.mipmap.secondgrade_upclass_18,R.mipmap.secondgrade_upclass_19
    };

    private String[] mClassroomStringNames2_1 = {
            "1、有疏密变化的线条", "2、有趣的刮画", "3、小花猫在睡觉", "4、丰收了","5、千姿百态的桥",
            "6、好看的器物", "7、新颖的小钟表", "8、电脑美术-我的新画板", "9、彩蝶","10、雪花飘飘",
            "11、我爱我家", "12、我的梦", "13、不倒翁", "14、看谁的风轮转得快","15、多彩的挂饰",
            "16、农民画中的节日", "17、节日里", "18、走进动漫世界", "19、头饰"
    };

    private Integer[] mClassroomImageIds2_2 = {
    };

    private String[] mClassroomStringNames2_2 = {
    };

    private Integer[] mClassroomImageIds3_1 = {
            R.mipmap.thirdgrade_upclass_1, R.mipmap.thirdgrade_upclass_2, R.mipmap.thirdgrade_upclass_3,R.mipmap.thirdgrade_upclass_4, R.mipmap.thirdgrade_upclass_5,
            R.mipmap.thirdgrade_upclass_6, R.mipmap.thirdgrade_upclass_7, R.mipmap.thirdgrade_upclass_8,R.mipmap.thirdgrade_upclass_9, R.mipmap.thirdgrade_upclass_10,
            R.mipmap.thirdgrade_upclass_11, R.mipmap.thirdgrade_upclass_12, R.mipmap.thirdgrade_upclass_13,R.mipmap.thirdgrade_upclass_14, R.mipmap.thirdgrade_upclass_15,
            R.mipmap.thirdgrade_upclass_16, R.mipmap.thirdgrade_upclass_17, R.mipmap.thirdgrade_upclass_18,R.mipmap.thirdgrade_upclass_19, R.mipmap.thirdgrade_upclass_20
    };

    private String[] mClassroomStringNames3_1 = {
            "1、色彩的渲染", "2、美丽的染纸", "3、四季如画", "4、吹塑纸版画","5、奔向未来",
            "6、画恐龙", "7、北京的胡同", "8、漫画欣赏(二)", "9、肖像漫画","10、面塑",
            "11、水墨游戏", "12、水墨游戏(二)", "13、故事乐园", "14、学画连环画","15、我们在游乐园里",
            "16、庙会", "17、巧用对称性", "18、字母的联想", "19、结网穿绳-花绳翻翻翻", "20、新年贺卡"
    };

    private Integer[] mClassroomImageIds3_2 = {
    };

    private String[] mClassroomStringNames3_2 = {
    };

    private Integer[] mClassroomImageIds4_1 = {
            R.mipmap.fourdgrade_upclass_1, R.mipmap.fourdgrade_upclass_2, R.mipmap.fourdgrade_upclass_3,R.mipmap.fourdgrade_upclass_4, R.mipmap.fourdgrade_upclass_5,
            R.mipmap.fourdgrade_upclass_6, R.mipmap.fourdgrade_upclass_7, R.mipmap.fourdgrade_upclass_8,R.mipmap.fourdgrade_upclass_9, R.mipmap.fourdgrade_upclass_10,
            R.mipmap.fourdgrade_upclass_11, R.mipmap.fourdgrade_upclass_12, R.mipmap.fourdgrade_upclass_13,R.mipmap.fourdgrade_upclass_14, R.mipmap.fourdgrade_upclass_15,
            R.mipmap.fourdgrade_upclass_16, R.mipmap.fourdgrade_upclass_17, R.mipmap.fourdgrade_upclass_18,R.mipmap.fourdgrade_upclass_19, R.mipmap.fourdgrade_upclass_20
    };

    private String[] mClassroomStringNames4_1 = {
            "1、我爱老师", "2、厨房一角", "3、画玩具", "4、庄严的牌楼","5、画牌楼",
            "6、科学与幻想", "7、我们的科技小报", "8、画家齐白石", "9、荷花","10、学画中国画-青蛙",
            "11、动物石膏版画", "12、石膏浮雕", "13、快快乐乐扭秧歌", "14、电子贺卡","15、多样的小饰品",
            "16、会动的纸造型", "17、手绘导游图", "18、剪挂笺过新年", "19、课本剧", "20、身边的壁画"
    };

    private Integer[] mClassroomImageIds4_2 = {
    };

    private String[] mClassroomStringNames4_2 = {
    };

    private Integer[] mClassroomImageIds5_1 = {
            R.mipmap.fivedgrade_upclass_1, R.mipmap.fivedgrade_upclass_2, R.mipmap.fivedgrade_upclass_3,R.mipmap.fivedgrade_upclass_4, R.mipmap.fivedgrade_upclass_5,
            R.mipmap.fivedgrade_upclass_6, R.mipmap.fivedgrade_upclass_7, R.mipmap.fivedgrade_upclass_8,R.mipmap.fivedgrade_upclass_9, R.mipmap.fivedgrade_upclass_10,
            R.mipmap.fivedgrade_upclass_11, R.mipmap.fivedgrade_upclass_12, R.mipmap.fivedgrade_upclass_13,R.mipmap.fivedgrade_upclass_14, R.mipmap.fivedgrade_upclass_15,
            R.mipmap.fivedgrade_upclass_16, R.mipmap.fivedgrade_upclass_17, R.mipmap.fivedgrade_upclass_18,R.mipmap.fivedgrade_upclass_19, R.mipmap.fivedgrade_upclass_20,
            R.mipmap.fivedgrade_upclass_21
    };

    private String[] mClassroomStringNames5_1 = {
            "1、易碎品的包装", "2、糕点盒设计", "3、细致的描绘", "4、美术家达利","5、奇妙的组合",
            "6、我设计的鞋", "7、各式各样的椅子", "8、实物版画", "9、展现瞬间之美","10、动态之美",
            "11、动态之美二", "12、动态之美三", "13、拼贴画", "14、彩绳装饰瓶","15、画门神",
            "16、中华世纪坛", "17、蔬果白描", "18、做个动画片", "19、国粹京剧", "20、京剧脸谱"
            , "21、京剧人物画"
    };

    private Integer[] mClassroomImageIds5_2 = {
    };

    private String[] mClassroomStringNames5_2 = {
    };

    private Integer[] mClassroomImageIds6_1 = {
            R.mipmap.sixdgrade_upclass_1, R.mipmap.sixdgrade_upclass_2, R.mipmap.sixdgrade_upclass_3,R.mipmap.sixdgrade_upclass_4, R.mipmap.sixdgrade_upclass_5,
            R.mipmap.sixdgrade_upclass_6, R.mipmap.sixdgrade_upclass_7, R.mipmap.sixdgrade_upclass_8,R.mipmap.sixdgrade_upclass_9, R.mipmap.sixdgrade_upclass_10,
            R.mipmap.sixdgrade_upclass_11, R.mipmap.sixdgrade_upclass_12, R.mipmap.sixdgrade_upclass_13,R.mipmap.sixdgrade_upclass_14, R.mipmap.sixdgrade_upclass_15,
            R.mipmap.sixdgrade_upclass_16, R.mipmap.sixdgrade_upclass_17, R.mipmap.sixdgrade_upclass_18
    };

    private String[] mClassroomStringNames6_1 = {
            "1、为自己设计动漫形象", "2、画皮影", "3、装饰色彩的魅力", "4、风筝","5、色彩纯度练习",
            "6、电脑美术——黏土动起来", "7、民间泥塑", "8、中国画——水仙花画法", "9、中国画——梅花画法","10、美的比例",
            "11、有趣的仿生设计", "12、瓶盖玩偶", "13、制作藏书票", "14、中国画——建筑画法","15、中国画",
            "16、情趣盎然的设计", "17、电脑美术照片美容", "18、我国的世界遗产"
    };

    private Integer[] mClassroomImageIds6_2 = {
    };

    private String[] mClassroomStringNames6_2 = {
    };

    private Integer[] mClassroomImageIds7_1 = {
            R.mipmap.sevengrade_upclass_0, R.mipmap.sevengrade_upclass_1, R.mipmap.sevengrade_upclass_2,R.mipmap.sevengrade_upclass_3, R.mipmap.sevengrade_upclass_4,
            R.mipmap.sevengrade_upclass_5, R.mipmap.sevengrade_upclass_6, R.mipmap.sevengrade_upclass_7,R.mipmap.sevengrade_upclass_8, R.mipmap.sevengrade_upclass_9,
            R.mipmap.sevengrade_upclass_10,R.mipmap.sevengrade_upclass_11
    };

    private String[] mClassroomStringNames7_1 = {
            "0、选修课中国美术馆",
            "1、线条的表现力", "2、绘画的构图", "3、用相机记录北京风情", "4、扮靓生活的大自然","5、源于自然的美丽纹样",
            "6、把大自然穿身上", "7、罗丹的雕塑", "8、动物立体造型", "9、观察与创造-动物的联想","10、幻想中的未来",
            "11、亚洲美术之旅"
    };

    private Integer[] mClassroomImageIds7_2 = {
    };

    private String[] mClassroomStringNames7_2 = {
    };

    private Integer[] mClassroomImageIds8_1 = {
            R.mipmap.eightgrade_upclass_0, R.mipmap.eightgrade_upclass_1, R.mipmap.eightgrade_upclass_2,R.mipmap.eightgrade_upclass_3, R.mipmap.eightgrade_upclass_4,
            R.mipmap.eightgrade_upclass_5, R.mipmap.eightgrade_upclass_6, R.mipmap.eightgrade_upclass_7,R.mipmap.eightgrade_upclass_8, R.mipmap.eightgrade_upclass_9,
            R.mipmap.eightgrade_upclass_10
    };

    private String[] mClassroomStringNames8_1 = {
            "0、故宫博物院（选修）",
            "1、明暗与立体", "2、李可染的山水画", "3、学画山水画", "4、设计改变生活","5、风格多样的台灯设计",
            "6、钟表的畅想", "7、废旧物改造", "8、北京中轴线建筑", "9、定格动画","10、非洲美术之旅",
    };

    private Integer[] mClassroomImageIds8_2 = {
    };

    private String[] mClassroomStringNames8_2 = {
    };

    private Integer[] mClassroomImageIds9_1 = {
            R.mipmap.ninegrade_upclass_0, R.mipmap.ninegrade_upclass_1, R.mipmap.ninegrade_upclass_2,R.mipmap.ninegrade_upclass_3, R.mipmap.ninegrade_upclass_4,
            R.mipmap.ninegrade_upclass_5, R.mipmap.ninegrade_upclass_6, R.mipmap.ninegrade_upclass_7,R.mipmap.ninegrade_upclass_8, R.mipmap.ninegrade_upclass_9,
            R.mipmap.ninegrade_upclass_10,R.mipmap.ninegrade_upclass_11
    };

    private String[] mClassroomStringNames9_1 = {
            "0、中国博物馆集萃—漫步中国的博物馆（选修）",
            "1、印象派绘画s", "2、绘画的空间表现", "3、画校园", "4、校园主题活动美术策划","5、校园主题活动美术设计",
            "6、书籍封面设计", "7、藏书票设计", "8、欣赏与创造—店铺门面设计", "9、京剧元素的再创造","10、数码照片处理",
            "11、大洋洲美术之旅"
    };

    private Integer[] mClassroomImageIds9_2 = {
    };

    private String[] mClassroomStringNames9_2 = {
    };

    //获取当前应该显示的图片
    public Integer[] getmImageIds(String type) {
//        System.out.println("当前获取图片数组:" + type);
        Integer[] resultImageIds = null;
        switch (type) {
            case "cover":
                resultImageIds = mCoverImageIds;
                break;
            case "1":
                resultImageIds = mClassroomImageIds1_1;
                break;
            case "2":
                resultImageIds = mClassroomImageIds1_2;
                break;
            case "3":
                resultImageIds = mClassroomImageIds2_1;
                break;
            case "4":
                resultImageIds = mClassroomImageIds2_2;
                break;
            case "5":
                resultImageIds = mClassroomImageIds3_1;
                break;
            case "6":
                resultImageIds = mClassroomImageIds3_2;
                break;
            case "7":
                resultImageIds = mClassroomImageIds4_1;
                break;
            case "8":
                resultImageIds = mClassroomImageIds4_2;
                break;
            case "9":
                resultImageIds = mClassroomImageIds5_1;
                break;
            case "10":
                resultImageIds = mClassroomImageIds5_2;
                break;
            case "11":
                resultImageIds = mClassroomImageIds6_1;
                break;
            case "12":
                resultImageIds = mClassroomImageIds6_2;
                break;
            case "13":
                resultImageIds = mClassroomImageIds7_1;
                break;
            case "14":
                resultImageIds = mClassroomImageIds7_2;
                break;
            case "15":
                resultImageIds = mClassroomImageIds8_1;
                break;
            case "16":
                resultImageIds = mClassroomImageIds8_2;
                break;
            case "17":
                resultImageIds = mClassroomImageIds9_1;
                break;
            case "18":
                resultImageIds = mClassroomImageIds9_2;
                break;

        }
        return resultImageIds;
    }

    //获取当前应该显示的标题
    public String[] getmStringNames(String type) {
        String[] resultStringNames = null;
        switch (type) {
            case "cover":
                resultStringNames = mCoverStringNames;
                break;
            case "1":
                resultStringNames = mClassroomStringNames1_1;
                break;
            case "2":
                resultStringNames = mClassroomStringNames1_2;
                break;
            case "3":
                resultStringNames = mClassroomStringNames2_1;
                break;
            case "4":
                resultStringNames = mClassroomStringNames2_2;
                break;
            case "5":
                resultStringNames = mClassroomStringNames3_1;
                break;
            case "6":
                resultStringNames = mClassroomStringNames3_2;
                break;
            case "7":
                resultStringNames = mClassroomStringNames4_1;
                break;
            case "8":
                resultStringNames = mClassroomStringNames4_2;
                break;
            case "9":
                resultStringNames = mClassroomStringNames5_1;
                break;
            case "10":
                resultStringNames = mClassroomStringNames5_2;
                break;
            case "11":
                resultStringNames = mClassroomStringNames6_1;
                break;
            case "12":
                resultStringNames = mClassroomStringNames6_2;
                break;
            case "13":
                resultStringNames = mClassroomStringNames7_1;
                break;
            case "14":
                resultStringNames = mClassroomStringNames7_2;
                break;
            case "15":
                resultStringNames = mClassroomStringNames8_1;
                break;
            case "16":
                resultStringNames = mClassroomStringNames8_2;
                break;
            case "17":
                resultStringNames = mClassroomStringNames9_1;
                break;
            case "18":
                resultStringNames = mClassroomStringNames9_2;
                break;
        }
        return resultStringNames;
    }
}
