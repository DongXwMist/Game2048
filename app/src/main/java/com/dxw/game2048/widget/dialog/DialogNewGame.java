package com.dxw.game2048.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dxw.game2048.R;

/**
 * 创建的自定义dialog,游戏结束窗口
 */
public class DialogNewGame extends Dialog {

    private Button btn_ok; //确定按钮
    private Button btn_cancel;//取消按钮
    private TextView tvTitle;//消息标题文本
    private TextView tvMessage;//消息提示文本
    private String strTitle;//从外界设置的title文本
    private String strMessage;//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private String okStr, cancelStr;
    private onCancelOnClickListener cancelOnClickListener;//取消按钮被点击了
    private onOkOnClickListener okOnClickListener;//确定按钮被点击了


    public DialogNewGame(@NonNull Context context) {
        super(context);
    }

    public DialogNewGame(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogNewGame(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public interface onCancelOnClickListener {
        void onCancelClick();
    }

    public interface onOkOnClickListener {
        void onOkClick();
    }


    /**
     * 设置取消按钮的显示内容和监听
     */
    public void setCancelOnClickListener(String str, onCancelOnClickListener cancelClickListener) {
        if (str != null) {
            cancelStr = str;
        }
        this.cancelOnClickListener = cancelClickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     */
    public void setOkOnClickListener(String str, onOkOnClickListener okClickListener) {
        if (str != null) {
            okStr = str;
        }
        this.okOnClickListener = okClickListener;
    }

    /**
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_game);
        //空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }

    private void initView() {
        btn_ok = (Button) findViewById(R.id.dialog_confirm);
        btn_cancel = (Button) findViewById(R.id.dialog_cancel);
        tvTitle = (TextView) findViewById(R.id.dialog_title);
        tvMessage = (TextView) findViewById(R.id.dialog_message);
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //入如果用户自定了title和message
        if (strTitle != null) {
            tvTitle.setText(strTitle);
        }
        if (strMessage != null) {
            tvMessage.setText(strMessage);
        }
        //如果设置按钮文字
        if (okStr != null) {
            btn_ok.setText(okStr);
        }
        if (cancelStr != null) {
            btn_cancel.setText(cancelStr);
        }
    }

    /**
     * 初始化界面的确定和取消监听
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (okOnClickListener != null) {
                    okOnClickListener.onOkClick();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancelOnClickListener != null) {
                    cancelOnClickListener.onCancelClick();
                }
            }
        });
    }

    /**
     * 从外界activity为dialog设置title
     */
    public void setTitle(String title) {
        strTitle = title;
    }

    /**
     * 从外界activity为dialog设置message
     */
    public void setMessage(String message) {
        strMessage = message;
        System.out.println("message is:" + message);
    }
}
