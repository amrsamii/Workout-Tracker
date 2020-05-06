package com.bestcoach.workouttracker.utils

import android.content.Context
import com.bestcoach.workouttracker.R
import org.tensorflow.lite.examples.posenet.lib.BodyPart
import org.tensorflow.lite.examples.posenet.lib.KeyPoint
import org.tensorflow.lite.examples.posenet.lib.Person
import kotlin.math.atan2


private const val ERROR_MARGIN: Double = 15.0

private var correctFlag: Int = 0

private val minConfidence = 0.5


data class Exercise(
    var name: String = "",

    var person: Person
)

fun pushExercise(person: Person): String {
    correctFlag = 0

    val leftShoulder: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_SHOULDER
    }

    val rightShoulder: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_SHOULDER
    }

    val rightElbow: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_ELBOW
    }

    val leftElbow: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_ELBOW
    }

    val R_shoulder_L_shoulder_x =
        rightShoulder!!.position.x - leftShoulder!!.position.x

    val R_shoulder_L_shoulder_y =
        rightShoulder.position.y - leftShoulder.position.y

    val R_shoulder_R_elbow_x = rightShoulder.position.x - rightElbow!!.position.x

    val R_shoulder_R_elbow_y = rightShoulder.position.y - rightElbow.position.y

    var Right_angle_push =
        atan2(R_shoulder_L_shoulder_y.toDouble(), R_shoulder_L_shoulder_x.toDouble()) -
                atan2(R_shoulder_R_elbow_y.toDouble(), R_shoulder_R_elbow_x.toDouble())

    val L_shoulder_R_shoulder_x =
        leftShoulder.position.x - rightShoulder.position.x

    val L_shoulder_R_shoulder_y =
        leftShoulder.position.y - rightShoulder.position.y

    val L_shoulder_L_elbow_x = leftShoulder.position.x - leftElbow!!.position.x

    val L_shoulder_L_elbow_y = leftShoulder.position.y - leftElbow.position.y

    var Left_angle_push = atan2(L_shoulder_L_elbow_y.toDouble(), L_shoulder_L_elbow_x.toDouble()) -
            atan2(L_shoulder_R_shoulder_y.toDouble(), L_shoulder_R_shoulder_x.toDouble())

    Left_angle_push = kotlin.math.abs(Left_angle_push * (180 / kotlin.math.PI))
    Right_angle_push = kotlin.math.abs(Right_angle_push * (180 / kotlin.math.PI))

    if (Left_angle_push > 180) {
        Left_angle_push = 360 - Left_angle_push
    }

    if (Right_angle_push > 180) {
        Right_angle_push = 360 - Right_angle_push
    }

    if ((Right_angle_push >= (135 + ERROR_MARGIN) || Right_angle_push <= (135 - ERROR_MARGIN)) || (Left_angle_push >= (135 + ERROR_MARGIN) || Left_angle_push <= (135 - ERROR_MARGIN))) {
        correctFlag = 0
        return "Your Elbows should be 45 degree from your body"

    } else if (correctFlag == 0) {
        correctFlag = 1
        return "You are correct"

    } else {
        return ""
    }
}

fun trackExercise(exercise: Exercise, context: Context): String {

    var status = ""

    if (exercise.person.score > minConfidence) {

        when (exercise.name) {
            context.getString(R.string.PUSH) -> {
                status = pushExercise(exercise.person)
            }
        }
    }

    return status
}