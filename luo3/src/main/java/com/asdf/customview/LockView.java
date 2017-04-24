package com.asdf.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by asdf on 2017/4/20.
 */

public class LockView extends View{


    private int width;
    private int height;

    //点的相关数据
    private int pointRadius = 40;
    private int pointGap = 40;
    private int pointBetween = pointGap + pointRadius * 2;
    private ArrayList<Paint> pointPaints = new ArrayList<>();

    private ArrayList<Point> points;

    //触摸线
    private Path myPath = new Path();
    private Paint pathPaint = new Paint();

    public void initPaint(){
        for(int i = 1;i<= 9;i++) {
            Paint pointPaint = new Paint();
            pointPaint.setColor(Color.GRAY);
            pointPaint.setAntiAlias(true);
            pointPaint.setDither(true);
            pointPaints.add(pointPaint);
        }
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setColor(Color.BLUE);
        pathPaint.setAntiAlias(true);
        pathPaint.setDither(true);
        pathPaint.setStrokeWidth(5);
    }

    public ArrayList<Point> initPoint(){
        ArrayList<Point> points = new ArrayList();
        //获取第一行第一个点的圆心坐标，其他8个点就可以根据该点的坐标进行计算
        //九宫格的边长，以圆心的坐标为矩形顶点的坐标
        int nineBian = pointRadius * 4 + pointGap * 2;
        int ox = (this.width - nineBian)/2 - pointBetween;
        int oy = (this.height - nineBian)/2;
        for (int i = 1;i<=9;i++){
            Point point = new Point();
            point.set(ox + (i%3 == 0?3:i%3) * pointBetween,oy);
            points.add(point);
            if(i%3 == 0) {
                oy += pointBetween;
            }
        }
        return points;
    }

    public LockView(Context context) {
        this(context, null);
    }

    public LockView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //初始化点的数据 点的坐标
        this.points = initPoint();
        for(int i = 0;i<points.size();i++){
            canvas.drawCircle(points.get(i).x,points.get(i).y,pointRadius,pointPaints.get(i));
        }
        drawMyPath(canvas);
    }
    private Point downPoint = new Point();
    private ArrayList<Integer> pathIndex = new ArrayList<>();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point curPoint = getValidatePoint(new Point((int)event.getX(),(int)event.getY()));//判断触摸点是否在九个圆中
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                pathIndex.clear();//每次滑动前清空路径
                if(curPoint != null){
                    downPoint = curPoint;
                    int index = points.indexOf(downPoint);
                    pathIndex.add(index);
                    myPath.moveTo(downPoint.x,downPoint.y);
                    pointPaints.get(index).setColor(Color.BLUE);
                }
                if(myPath.isEmpty()){
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                myPath.reset();
                if(curPoint == null || pathIndex.contains(points.indexOf(curPoint))){
                    myPath.moveTo(downPoint.x,downPoint.y);
                    myPath.lineTo(event.getX(),event.getY());
                    invalidate();
                    break;
                }
                if(!pathIndex.contains(curPoint)) {
                    int index = points.indexOf(curPoint);
                    pathIndex.add(index);
                    judgePointInLine(downPoint,curPoint);
                    downPoint.set(curPoint.x, curPoint.y);//记录该点
                }
                break;
            case MotionEvent.ACTION_UP:
                myPath.reset();
                pathIndex.clear();
                resetPaint(Color.GRAY);
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }


    /**
     * 判断两点之间是否还有圆点
     * @param start
     * @param end
     */
    public void judgePointInLine(Point start,Point end){
        int A = (-end.y) - (-start.y);
        int B = (-start.x) - (-end.x);
        int C = (-end.x) * (-start.y) - (-start.x) * (-end.y);
        for(Point temp : points){
            if(temp.equals(start) || temp.equals(end)) continue;
            int mX = -temp.x;
            int mY = -temp.y;
            if(Math.pow(A * mX + B * mY + C,2) / (A * A + B * B) < pointRadius * pointRadius &&
                    temp.x>=Math.min(start.x,end.x)&&
                    temp.x<=Math.max(start.x,end.x)&&
                    temp.y>=Math.min(start.y,end.y)&&
                    temp.y<=Math.max(start.y,end.y)
                    ){
                if(!pathIndex.contains(points.indexOf(temp))) {
                    pathIndex.add(pathIndex.indexOf(points.indexOf(start)) + 1,points.indexOf(temp));
                    break;
                }
            }
        }
    }

    /**
     * 判断当前点是否在某圆点的范围内，如果在则返回指定Point对象
     * @param about
     * @return
     */
    public Point getValidatePoint(Point about){
        Point temp = null;
        for(Point point : points){
            int mX = point.x;
            int mY = point.y;
            if((about.x - mX)*(about.x - mX) + (about.y - mY)*(about.y - mY) < pointRadius * pointRadius){
                temp = new Point(mX,mY);
                break;
            }
        }
        return temp;
    }

    public void drawMyPath(Canvas canvas){
        canvas.drawPath(this.myPath,pathPaint);
        Path tempPath = new Path();
        for(int i = 0,n = pathIndex.size();i<n;i++){
            Point point = points.get(pathIndex.get(i));
            pointPaints.get(pathIndex.get(i)).setColor(Color.BLUE);
            if(i == 0){
                tempPath.moveTo(point.x,point.y);
            }else{
                tempPath.lineTo(point.x,point.y);
            }
        }
        canvas.drawPath(tempPath,pathPaint);
    }

    public void resetPaint(int color){
        for (Paint paint : pointPaints){
            paint.setColor(color);
        }
    }
}
