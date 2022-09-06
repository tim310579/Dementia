package com.example.New_Dementia_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Fill_in_reference extends AppCompatActivity {
    private TextView tv_fill_in_reference;
    private Button [] bn_refs = new Button[14];
    private Button bn_back;
    private String[] str_ref_title = {"", "妄想", "幻覺", "激動/攻擊性", "憂鬱/情緒不佳", "焦慮", "昂然自得/欣快感",
                    "冷漠/毫不在意", "言行失控", "暴躁易怒/情緒易變", "怪異動作", "睡眠/夜間行為", "食慾/飲食行為改變", "嚴重度與困擾程度填表說明"};
    private String[] str_refs = {" ", "有一些你知道與事實不符的想法？所指的不只是他懷疑而已，而是病人堅信他曾遭遇到這些事情。舉例:\n" +
                    "(1)\t相信他正處在危險之中，或有人正在企圖要傷害他？\n" +
                    "(2)\t相信有人企圖要偷他的東西？\n" +
                    "(3)\t相信他的配偶有外遇？\n" +
                    "(4)\t相信有不受歡迎的客人正住在他家裡？\n" +
                    "(5)\t相信他的配偶或其他人不是他們所聲稱的人？\n" +
                    "(6)\t相信他住的房子不是他的家？\n" +
                    "(7)\t相信他的家人準備要遺棄他？\n" +
                    "(8)\t相信電視上或雜誌上的人物真的存在這房裡？（試著和他們說話或互動？）\n",

            "病人說出或表現出他看到或聽到並不存在的事情？這裡不是指錯誤的信念，而是病人的確經歷過不正常的聲音或影像。舉例:\n" +
                    "(1)\t描述他聽到聲音或表現得好像他在聽聲音？\n" +
                    "(2)\t和某一位並不在現場的人交談？\n" +
                    "(3)\t描述他看到其他人看不到的事物，或表現的好像他正看見其他人看不到的事物（人、動物、光等等）？\n" +
                    "(4)\t向你報告他聞到一些其他人沒有嗅到的氣味？\n" +
                    "(5)\t描述他感覺到皮膚上有東西，或是表現出像是他感覺到有東西在爬或在觸摸他？\n" +
                    "(6)\t描述他有不明原因的味覺？\n",


            "拒絕配合或不願讓別人幫助他？很難處理？舉例:\n" +
                    "(1)\t對照顧他的人生氣，或拒絕一些日常生活活動，如洗澡、更換衣服？\n" +
                    "(2)\t頑固的，必須要依照他的意見去做？\n" +
                    "(3)\t拒絕或不配合他人的幫忙？\n" +
                    "(4)\t有出現任何行為使得他變得難以處理？\n" +
                    "(5)\t很憤怒的大聲咆哮或罵人？\n" +
                    "(6)\t用力甩門，踢家具，摔東西？\n" +
                    "(7)\t有蓄意去傷害或打別人？\n",

            "顯得悲傷或憂鬱？向你說到他的心情悲傷或憂鬱？舉例:\n" +
                    "(1)\t有流淚或啜泣，顯示他很悲傷？\n" +
                    "(2)\t說出或表現出自己很憂傷或心情不好？\n" +
                    "(3)\t貶低自己或覺得自己是個失敗者？\n" +
                    "(4)\t說他是個壞人或罪有應得的人？\n" +
                    "(5)\t顯得很沮喪或說他對未來沒抱希望？\n" +
                    "(6)\t說他是家人的負擔，或說家庭若沒有他會變的好些？\n" +
                    "(7)\t表達想死的意願，或談論過自殺的事情？\n",

            "沒有明顯原因就會神經緊張、擔心或害怕？顯得緊張或忐忑不安？害怕和你分開？舉例:\n" +
                    "(1)\t一直說他擔心已經計畫好的事情？\n" +
                    "(2)\t感覺全身顫抖，不能放輕鬆，或感到特別緊張？\n" +
                    "(3)\t感到或抱怨過會呼吸急促，喘不過氣來，嘆息等，但是除了神經緊張外找不到其他可能原因？\n" +
                    "(4)\t抱怨緊張得坐立不安、胃漲氣或心臟跳得很快或很重，常與緊張不安有關（症狀無法用身體不佳來解釋）？\n" +
                    "(5)\t避開使他更心神不安的場所或情境，如乘車、和朋友相聚或在人群中？\n" +
                    "(6)\t和你（或是他的照顧者）分開時，他感到神經緊張或生氣，他黏著你不要和你分開？ \n",

            "無緣無故地特別高興快樂？並非見到朋友，收到禮物或和家人相處時正常應有的快樂。而是不尋常的、持續的好心情，或他會對一些事情覺得有趣，但別人並不如此認為。舉例:\n" +
                    "(1)\t顯得心情太好了或太快樂了，和他平時不一樣？\n" +
                    "(2)\t對別人不會感到好笑的事情而自以為幽默或好笑？\n" +
                    "(3)\t做出孩子式的幽默，常會不適當的咯咯或哈哈大笑（如對別人不幸的遭遇）？\n" +
                    "(4)\t說些別人不認為好笑而自己特別覺得好笑的笑話？\n" +
                    "(5)\t開小孩式的玩笑，如為了好玩而偷捏別人，或故意躲起來？\n" +
                    "(6)\t說「大話」，或聲稱自己擁有誇大的能力或財富？\n",

            "對周遭世界失去興趣？對做事失去興趣？也沒有原動力去從事新的活動？不易與人交談或做雜務(家事)？顯得表情冷漠或態度冷淡？舉例:\n" +
                    "(1)\t和平時比起來，顯得較不主動也較不活躍？\n" +
                    "(2)\t不願意與人主動交談？\n" +
                    "(3)\t顯得缺少熱情或缺乏感情？\n" +
                    "(4)\t較少參與做家務雜事？\n" +
                    "(5)\t對他人的活動或計畫顯得較沒有興趣？\n" +
                    "(6)\t對家人或朋友變得不關心注意？\n" +
                    "(7)\t對他平時的喜好（興趣）顯得比較不熱心？\n",

            "顯得做事衝動欠缺考慮？說一些或做一些平時在公眾下不該說或不該做的事？做一些事讓你或其他人覺得很尷尬？舉例:\n" +
                    "(1)\t表現得很衝動，完全不考慮後果？\n" +
                    "(2)\t和完全陌生的人交談，好像他是認識他們的？\n" +
                    "(3)\t和他人談話時，是否不理會他人的感覺，或傷害他人的感覺？\n" +
                    "(4)\t說些低級黃色言論或有關性的言論，這些平時都不會公開討論的？\n" +
                    "(5)\t很公開的討論每一個人的隱私或私人事情，這些平時都不會公開討論的？\n" +
                    "(6)\t調戲、碰觸或擁抱他人，超出他平時的個性？\n",

            "是否有生氣？心情善變？不正常的失去耐性？這裡不是指因為喪失記憶力或不能從事一般性工作而呈現的挫折感，這裡所指的是病人是否有不正常的易怒、失去耐性及快速的情緒改變，和他平時完全不一樣。舉例:\n" +
                    "(1)\t脾氣很壞，為小事而勃然大怒？\n" +
                    "(2)\t很快的改變情緒，前一分鐘心情好，一下子馬上就生氣？\n" +
                    "(3)\t有出現短暫但劇烈的生氣？\n" +
                    "(4)\t失去耐性，常無法忍受延誤或等待已經計畫好的活動？\n" +
                    "(5)\t很情緒化，或暴躁易怒？\n" +
                    "(6)\t喜歡和人爭論而且難以相處？ \n",

            "病人不斷的重複去做某一件事，如來回走來走去、開櫃子、開抽屜、或不斷的捏東西，纏繞繩線？舉例:\n" +
                    "(1)\t在房子裡沒明顯得目的走來走去？\n" +
                    "(2)\t在尋找東西，把抽屜或櫃子打開，把東西拆開？重覆把抽屜或櫃子打開，尋找東西？\n" +
                    "(3)\t重覆地把衣服穿上又脫下？\n" +
                    "(4)\t有一些重覆的動作，或他們不斷重複再做的『習慣』？\n" +
                    "(5)\t做一些重複的動作如扣上又扣下，捏來捏去，繩線繞來繞去？\n" +
                    "(6)\t極度不安，似乎不能安坐，或不斷抖動他的腳，不斷的輕拍手指？ \n",

            "病人有睡眠問題，像是晚上會起來？半夜會遊走，穿好衣服或妨礙到你的睡眠？舉例:\n" +
                    "(1)\t不易入睡？\n" +
                    "(2)\t半夜起床(如果病人只是半夜起來上廁所一兩次，而又很快的入睡，則不列為睡眠問題)？\n" +
                    "(3)\t半夜遊走，漫步或做一些不適當的活動？\n" +
                    "(4)\t半夜會吵醒你？\n" +
                    "(5)\t會起床、穿衣服然後準備外出，以為現在就是早上要開始一天的工作？\n" +
                    "(6)\t起來太早(比他平日要早些)？\n" +
                    "(7)\t在白天睡的太多？\n",

            "病人有食慾、體重及進食習慣改變(如果病人因為失能而需要餵食時， 則此項不做紀錄)? 喜愛的食物是否改變？舉例:\n" +
                    "(1)\t食慾是否減低？\n" +
                    "(2)\t食慾是否增加？ \n" +
                    "(3)\t「吃相」改變？如嘴巴裡塞得滿滿的。\n" +
                    "(4)\t選擇的食物是否改變？如吃太多的甜食或其他特別種類的食物。\n",


            "請您依照收案病人的日常生活身心狀態進行評估，盡可能的填寫正確的時間與持續時間。\n" +
                    "一、嚴重度(對個案造成之影響)\n" +
                    "1.\t輕度=稍有不同，但沒有明顯改變\n" +
                    "2.\t中度=明顯改變，尚未達到極度改變\n" +
                    "3.\t重度=非常極度或顯著改變\n" +
                    "\n" +
                    "二、困擾程度(這個行為引起您情緒上的困擾有多嚴重？)\n" +
                    "0.\t完全不會造成困擾(感到有些困擾，但可以處理)。\n" +
                    "1.\t有一點困擾(沒有非常困擾，可以輕鬆處理)。\n" +
                    "2.\t輕度困擾(沒有非常困擾，可以輕鬆處理)。\n" +
                    "3.\t中度困擾(有些困擾，並非每次都可以輕鬆處理)。\n" +
                    "4.\t重度困擾(非常困擾，難以處理)。\n" +
                    "5.\t非常嚴重困擾(極度困擾，無法處理)\n" +
                    "\n" +
                    "三、特別說明:\n" +
                    "\t如您認為有一個以上的狀態同時存在，您亦可勾選一個以上的項目。本資料為精神神經量表之行為症狀評估的參考，舉出的例子僅作為該項目概念的參考，病人的行為模式或表達的言語，不需要與文件中舉例完全一致，若出現相似的身心行為的情況即可填答該項目。\n" +
                    "\n" +
                    "\t您也可參考影片示範，該影片是台大醫院精神科黃宗正醫師示範量表施測方式(共有三個影片):\n" +
                    "影片1.連結 https://youtu.be/7l8QUpUuVdU \n" +
                    "影片2.連結 https://youtu.be/pkClad2HfEo \n" +
                    "影片3.連結 https://youtu.be/PE-TEsuaDQk \n" +
                    "\n" +
                    "\t如您有任何疑慮，請向收案工作人員反映，請記得留下您的姓名與聯絡方式，我們將盡快與您聯絡:\n" +
                    "小港醫院神經科醫師，謝升文醫師 circle543@yahoo.com.tw ；\n" +
                    "收案研究助理，林亭儀小姐 tilin201909@gmail.com  。\n" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_in_reference);

        tv_fill_in_reference = (TextView) findViewById(R.id.tv_fill_in_reference);
        tv_fill_in_reference.setText(Html.fromHtml("<u>"+"填答參考"+"</u>"));
        bn_refs[1] = (Button) findViewById(R.id.bn_ref1);
        bn_refs[2] = (Button) findViewById(R.id.bn_ref2);
        bn_refs[3] = (Button) findViewById(R.id.bn_ref3);
        bn_refs[4] = (Button) findViewById(R.id.bn_ref4);
        bn_refs[5] = (Button) findViewById(R.id.bn_ref5);
        bn_refs[6] = (Button) findViewById(R.id.bn_ref6);
        bn_refs[7] = (Button) findViewById(R.id.bn_ref7);
        bn_refs[8] = (Button) findViewById(R.id.bn_ref8);
        bn_refs[9] = (Button) findViewById(R.id.bn_ref9);
        bn_refs[10] = (Button) findViewById(R.id.bn_ref10);
        bn_refs[11] = (Button) findViewById(R.id.bn_ref11);
        bn_refs[12] = (Button) findViewById(R.id.bn_ref12);
        bn_refs[13] = (Button) findViewById(R.id.bn_degree_detail);
        bn_back = (Button) findViewById(R.id.bn_back);
        bn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(Fill_in_reference.this, Symptom_choose.class);
                startActivity(intent);

            }

        });
        //bn_back.setOnClickListener(new ButtonClickListener());
        for(int i = 1; i < 14; i++) {
            final int j = i;
            bn_refs[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(Fill_in_reference.this);
                    dialog.setContentView(R.layout.popup_fill_in_reference);

                    TextView tv_ref_title = (TextView) dialog.findViewById(R.id.tv_ref_title);
                    TextView tv_ref_detail = (TextView) dialog.findViewById(R.id.tv_ref_detail);
                    tv_ref_title.setText(str_ref_title[j]);
                    tv_ref_detail.setText(str_refs[j]);
                    if (j == 13){
                        tv_ref_detail.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                    Button bn_check = (Button) dialog.findViewById(R.id.bn_check);
                    //Button bn_check_to_leave = (Button) dialog.findViewById(R.id.bn_check_to_leave);
                    bn_check.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    /*
                    bn_check_to_leave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(Symptom_choose.this  , Check_history.class);
                            startActivity(intent);
                        }
                    });

                     */

                    dialog.show();
                }
            });
        }
    }
}