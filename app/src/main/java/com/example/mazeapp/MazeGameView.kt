package com.example.mazeapp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MazeGameView : AppCompatActivity() {

    private lateinit var blueDot: View
    private lateinit var redGoal: View
    private var pathViews: List<View> = listOf()

    private var offsetX = 0f
    private var offsetY = 0f
    private var startX = 0f
    private var startY = 0f

    private var currentLevel = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentLevel = intent.getIntExtra("LEVEL", 1)

        if (currentLevel == 1) {
            setContentView(R.layout.maze_level1)
            blueDot = findViewById(R.id.start)
            redGoal = findViewById(R.id.end)
            pathViews = listOf(
                findViewById(R.id.vertical_path),
                findViewById(R.id.horizontal_path)
            )
        } else {
            setContentView(R.layout.maze_level2)
            blueDot = findViewById(R.id.start)
            redGoal = findViewById(R.id.end)
            pathViews = listOf(
                findViewById(R.id.left_path),
                findViewById(R.id.left_path2),
                findViewById(R.id.right_path),
                findViewById<View>(R.id.level2_layout).findViewWithTag("horizontal1"),
                findViewById<View>(R.id.level2_layout).findViewWithTag("horizontal2"),
                findViewById<View>(R.id.level2_layout).findViewWithTag("horizontal3"),
                findViewById<View>(R.id.level2_layout).findViewWithTag("horizontal4")
            )
        }

        blueDot.post {
            startX = blueDot.x
            startY = blueDot.y
        }

        blueDot.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    offsetX = event.x
                    offsetY = event.y
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX - offsetX
                    val newY = event.rawY - offsetY

                    val simulatedRect = Rect(
                        newX.toInt(),
                        newY.toInt(),
                        (newX + v.width).toInt(),
                        (newY + v.height).toInt()
                    )

                    if (!isFullyInsideAnyPath(simulatedRect)) {
                        resetPosition()
                        return@setOnTouchListener false
                    }

                    if (isOverlapping(simulatedRect, redGoal)) {
                        showCongratulations()
                        return@setOnTouchListener false
                    }

                    v.x = newX
                    v.y = newY
                    true
                }

                else -> false
            }
        }
    }

    private fun resetPosition() {
        blueDot.x = startX
        blueDot.y = startY
    }

    private fun showCongratulations() {
        resetPosition()

        AlertDialog.Builder(this)
            .setTitle("Congratulations!")
            .setMessage("You reached the goal!")
            .setCancelable(false)
            .setPositiveButton("Next Level") { _, _ ->
                if (currentLevel == 2) {
                    val intent = Intent(this, EskeriPix::class.java)
                    startActivityForResult(intent, 100)
                } else {
                    val nextLevel = currentLevel + 1
                    val intent = Intent(this, MazeGameView::class.java)
                    intent.putExtra("LEVEL", nextLevel)
                    startActivity(intent)
                    finish()
                }
            }
            .setNegativeButton("Exit") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val nextLevel = currentLevel + 1
            val intent = Intent(this, MazeGameView::class.java)
            intent.putExtra("LEVEL", nextLevel)
            startActivity(intent)
            finish()
        }
    }

    private fun getViewRectOnScreen(view: View): Rect {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return Rect(
            location[0],
            location[1],
            location[0] + view.width,
            location[1] + view.height
        )
    }

    private fun isFullyInsideAnyPath(dotRect: Rect): Boolean {
        return pathViews.any { pathView ->
            val pathRect = getViewRectOnScreen(pathView)
            pathRect.contains(dotRect)
        }
    }

    private fun isOverlapping(dotRect: Rect, targetView: View): Boolean {
        val targetRect = getViewRectOnScreen(targetView)
        return Rect.intersects(dotRect, targetRect)
    }
}
