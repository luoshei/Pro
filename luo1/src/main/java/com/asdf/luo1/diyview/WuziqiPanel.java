package com.asdf.luo1.diyview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.asdf.luo1.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by asdf on 2017/4/17.
 */

public class WuziqiPanel extends View{

    private int MAX_NUM = 5;

    private int panelWidth;
    private Paint panelPaint;

    private int lineNum = 10;
    private float lineHeight;

    private Bitmap whitePiece;
    private Bitmap blackPiece;

    private boolean blackFirst = true;

    private ArrayList<Point> whites = new ArrayList<>();
    private ArrayList<Point> blacks = new ArrayList<>();

    private float ratioPieceOfLineHeight = 3 * 1.0f / 4;
    private boolean gameOver;

    public WuziqiPanel(Context context) {
        this(context,null);
    }

    public WuziqiPanel(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WuziqiPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(0x99FF0000);

        whitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.white);
        blackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.black);
        panelPaint = new Paint();
        panelPaint.setStyle(Paint.Style.STROKE);
        panelPaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);

        if(widthMode == MeasureSpec.UNSPECIFIED){
            width = heightSize;
        }else if(heightMode == MeasureSpec.UNSPECIFIED){
            width = widthSize;
        }
        setMeasuredDimension(width,width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.panelWidth = w;
        this.lineHeight = w / lineNum;

        whitePiece = Bitmap.createScaledBitmap(whitePiece,(int)(whitePiece.getWidth() * 0.2),
                (int)(whitePiece.getHeight() * 0.2),false);
        blackPiece = Bitmap.createScaledBitmap(blackPiece,(int)(blackPiece.getWidth() * 0.2),
                (int)(blackPiece.getHeight() * 0.2),false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBoard(canvas);
        drawPieces(canvas);

    }

    public void checkGameOver(Point p, List<Point> points){
        String[] s = new String[]{"白棋胜利","黑棋胜利"};
        if(checkHorizontal(p,points) || checkVerticle(p,points) || checkDiagonalRight(p,points) || checkDiagonalLeft(p,points) ){
            Toast.makeText(this.getContext(),"gameover" + (points == whites?s[0]:s[1]),Toast.LENGTH_SHORT).show();
            gameOver = true;
        }
    }

    public void reBegin(){
        whites.clear();
        blacks.clear();
        blackFirst = true;
        gameOver = false;
        postInvalidate();
    }


    private boolean checkVerticle(Point p, List<Point> points) {
        int x = p.x;
        int y = p.y;
        int count = 1;
        for (int i = 1;i<MAX_NUM;i++){
            if(points.contains(new Point(x,y-i)))
                count++;
            else
                break;
        }
        for (int i = 1;i<MAX_NUM;i++){
            if(points.contains(new Point(x,y+i)))
                count++;
            else
                break;
        }
        if(count >= MAX_NUM)
            return true;
        return false;
    }

    private boolean checkHorizontal(Point p, List<Point> points) {
        int x = p.x;
        int y = p.y;
        int count = 1;
        for (int i = 1;i<MAX_NUM;i++){
            if(points.contains(new Point(x-i,y)))
                count++;
            else
                break;
        }
        for (int i = 1;i<MAX_NUM;i++){
            if(points.contains(new Point(x+i,y)))
                count++;
            else
                break;
        }
        if(count >= MAX_NUM)
            return true;
        return false;
    }

    private boolean checkDiagonalRight(Point p, List<Point> points) {
        int x = p.x;
        int y = p.y;
        int count = 1;
        for (int i = 1;i<MAX_NUM;i++){
            if(points.contains(new Point(x-i,y-i)))
                count++;
            else
                break;
        }
        for (int i = 1;i<MAX_NUM;i++){
            if(points.contains(new Point(x+i,y+i)))
                count++;
            else
                break;
        }
        if(count >= MAX_NUM)
            return true;
        return false;
    }

    private boolean checkDiagonalLeft(Point p, List<Point> points) {
        int x = p.x;
        int y = p.y;
        int count = 1;
        for (int i = 1;i<MAX_NUM;i++){
            if(points.contains(new Point(x-i,y+i)))
                count++;
            else
                break;
        }
        for (int i = 1;i<MAX_NUM;i++){
            if(points.contains(new Point(x+i,y-i)))
                count++;
            else
                break;
        }
        if(count >= MAX_NUM)
            return true;
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gameOver) return true;
        if(event.getAction() == MotionEvent.ACTION_DOWN){

            int x = (int) event.getX();
            int y = (int) event.getY();
            Log.e(TAG, "onTouchEvent: " + "x=" + x + " y=" + y );
            Point point = getValidate(x,y);

            if(whites.contains(point) || blacks.contains(point))return false;

            if(blackFirst){
                blacks.add(point);
                checkGameOver(point,blacks);
            }else{
                whites.add(point);
                checkGameOver(point,whites);
            }
            blackFirst = !blackFirst;
            postInvalidate();

             return true;
        }

        return super.onTouchEvent(event);
    }

    private Point getValidate(int x, int y) {
        return new Point((int)(x / lineHeight), (int)(y / lineHeight));
    }

    private void drawPieces(Canvas canvas) {
        for (int i = 0,n = whites.size();i<n;i++){
            Point whitePoint = whites.get(i);
            canvas.drawBitmap(whitePiece,(whitePoint.x + (1 - ratioPieceOfLineHeight)/2) * lineHeight,
                    (whitePoint.y + (1 - ratioPieceOfLineHeight)/2) * lineHeight,null);
        }
        for (int i = 0,n = blacks.size();i<n;i++){
            Point whitePoint = blacks.get(i);
            canvas.drawBitmap(blackPiece,(whitePoint.x + (1 - ratioPieceOfLineHeight)/2) * lineHeight,
                    (whitePoint.y + (1 - ratioPieceOfLineHeight)/2) * lineHeight,null);
        }
    }

    private void drawBoard(Canvas canvas) {
        for (int i = 0;i<lineNum;i++){
            int startX = (int)(lineHeight / 2);
            int stopX = (int) (panelWidth-lineHeight / 2);
            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX,y,stopX,y,panelPaint);
            canvas.drawLine(y,startX,y,stopX,panelPaint);
        }
    }


    private static final String INSTANCE = "INSTANCE";
    private static final String WHITES = "WHITES";
    private static final String BLACKS = "BLACKS";
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());

        bundle.putParcelableArrayList(WHITES,whites);
        bundle.putParcelableArrayList(BLACKS,blacks);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof  Bundle){
            Bundle bundle = (Bundle)state;
            whites = bundle.getParcelableArrayList(WHITES);
            blacks = bundle.getParcelableArrayList(BLACKS);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }

        super.onRestoreInstanceState(state);
    }
}
