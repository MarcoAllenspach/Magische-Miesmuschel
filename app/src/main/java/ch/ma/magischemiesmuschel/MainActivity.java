package ch.ma.magischemiesmuschel;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final float OFFSET_GRIFF_X = 50;
    private final float OFFSET_GRIFF_Y = 40;
    private final int TIME_MOVE_BACK_MS = 1000;
    public ImageView griffView;
    private DrawView drawView;
    float griffX, griffY, x, y;
    boolean dragEnabled = false;
    boolean movementFinish = true;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        griffView=findViewById(R.id.griff);

        drawView = findViewById(R.id.drawView);

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.intro);
        mediaPlayer.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isViewTouched(griffView, event.getRawX(), event.getRawY()) && movementFinish) {
                    if (griffX == 0 && griffY == 0) {
                        griffX = griffView.getX();
                        griffY = griffView.getY();
                    }
                    x = griffView.getX() - event.getRawX();
                    y = griffView.getY() - event.getRawY();
                    dragEnabled = true;
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.aufziehen);
                    mediaPlayer.start();
                    return true;
                }

            case MotionEvent.ACTION_MOVE:
                if (dragEnabled) {
                    griffView.animate()
                            .x(event.getRawX() + x)
                            .y(event.getRawY() + y)
                            .setDuration(0)
                            .start();
                    drawView.drawLine(griffX + OFFSET_GRIFF_X, griffY + OFFSET_GRIFF_Y,
                            event.getRawX() + x + OFFSET_GRIFF_X + 10, event.getRawY() + y + OFFSET_GRIFF_Y + 10);

                }
                return true;

            case MotionEvent.ACTION_UP:
                if (dragEnabled) {
                    chooseText();
                    movementFinish = false;
                    dragEnabled = false;

                    griffView.animate()
                            .x(griffX)
                            .y(griffY)
                            .setDuration(TIME_MOVE_BACK_MS)
                            .withEndAction(() -> {
                                movementFinish = true;
                                drawView.clearCanvas();
                            })
                            .start();

                    // Animate the line on drawView
                    float startX = event.getRawX() + x;
                    float startY = event.getRawY() + y;
                    ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
                    animator.setDuration(TIME_MOVE_BACK_MS);
                    animator.addUpdateListener(animation -> {
                        float fraction = animation.getAnimatedFraction();
                        float newX = startX + fraction * (griffX - startX);
                        float newY = startY + fraction * (griffY - startY);
                        drawView.drawLine(griffX + OFFSET_GRIFF_X, griffY + OFFSET_GRIFF_Y,
                                newX + OFFSET_GRIFF_X + 10, newY + OFFSET_GRIFF_Y + 10);
                    });
                    animator.start();
                }
                return true;

            default:
                return false;
        }

    }

    private boolean isViewTouched(View view, float touchX, float touchY) {
        // Expand the hit area by adding 30 pixels to each side
        Rect rect = new Rect();
        view.getHitRect(rect);

        rect.left -= 30;
        rect.top -= 30;
        rect.right += 30;
        rect.bottom += 150;

        return rect.contains((int) touchX, (int) touchY);
    }


    public void chooseText() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        Random random = new Random();
        int value = random.nextInt(4);

        switch (value){
            case 0:
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.nein);
                break;
            case 1:
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.ja);
                break;
            case 2:
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.eines_tages_vieleicht);
                break;
            case 3:
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.fragdocheinfachnochmal);
                break;
            default:
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.fragdocheinfachnochmal);
                break;
        }

        mediaPlayer.start();

    }
}