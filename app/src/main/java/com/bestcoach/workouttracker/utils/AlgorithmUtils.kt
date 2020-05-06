package com.bestcoach.workouttracker.utils

import android.content.Context
import com.bestcoach.workouttracker.R
import org.tensorflow.lite.examples.posenet.lib.BodyPart
import org.tensorflow.lite.examples.posenet.lib.KeyPoint
import org.tensorflow.lite.examples.posenet.lib.Person
import kotlin.math.abs
import kotlin.math.atan2

data class Exercise(
    var name: String = "",

    var person: Person
)

enum class Exercises {
    NO_EXERCISE, PUSH, PULL, PLANK, SIDE_PLANK
}

private const val ERROR_MARGIN: Double = 15.0

private var correctFlag: Int = 0

private val minConfidence = 0.5

private var currentExercise: Exercises = Exercises.NO_EXERCISE


fun pushExercise(person: Person): String {
    currentExercise = Exercises.PUSH

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

    Left_angle_push = abs(Left_angle_push * (180 / kotlin.math.PI))
    Right_angle_push = abs(Right_angle_push * (180 / kotlin.math.PI))

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

fun pullExercise(person: Person): String {
    currentExercise = Exercises.PULL

    val leftAnkle: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_ANKLE
    }

    val rightAnkle: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_ANKLE
    }

    val leftHip: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_HIP
    }
    val rightHip: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_HIP
    }
    val leftKnee: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_KNEE
    }

    val rightKnee: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_KNEE
    }

    val R_knee_R_hip_x = rightKnee!!.position.x - rightHip!!.position.x

    val R_knee_R_hip_y = rightKnee.position.y - rightHip.position.y

    val R_knee_R_ankle_x = rightKnee.position.x - rightAnkle!!.position.x

    val R_knee_R_ankle_y = rightKnee.position.y - rightAnkle.position.y

    var Right_angle_pull = atan2(R_knee_R_hip_y.toDouble(), R_knee_R_hip_x.toDouble()) -
            atan2(R_knee_R_ankle_y.toDouble(), R_knee_R_ankle_x.toDouble())

    val L_knee_L_hip_x = leftKnee!!.position.x - leftHip!!.position.x

    val L_knee_L_hip_y = leftKnee.position.y - leftHip.position.y

    val L_knee_L_ankle_x = leftKnee.position.x - leftAnkle!!.position.x

    val L_knee_L_ankle_y = leftKnee.position.y - leftAnkle.position.y

    atan2(L_knee_L_ankle_y.toDouble(), L_knee_L_ankle_x.toDouble())
    var Left_angle_pull =
        atan2(L_knee_L_ankle_y.toDouble(), L_knee_L_ankle_x.toDouble()) -
                atan2(L_knee_L_hip_y.toDouble(), L_knee_L_hip_x.toDouble())

    Left_angle_pull = abs(Left_angle_pull * (180 / kotlin.math.PI))
    Right_angle_pull = abs(Right_angle_pull * (180 / kotlin.math.PI))

    if (Left_angle_pull > 180) {
        Left_angle_pull = 360 - Left_angle_pull
    }

    if (Right_angle_pull > 180) {
        Right_angle_pull = 360 - Right_angle_pull
    }


    if ((Right_angle_pull >= (180 + ERROR_MARGIN) || Right_angle_pull <= (180 - ERROR_MARGIN)) || (Left_angle_pull >= (180 + ERROR_MARGIN) || Left_angle_pull <= (180 - ERROR_MARGIN))) {
        correctFlag = 0
        return "Straight your leg"
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
                if (currentExercise != Exercises.PUSH) correctFlag = 0
                status = pushExercise(exercise.person)
            }

            context.getString(R.string.PULL) -> {
                if (currentExercise != Exercises.PULL) correctFlag = 0
                status = pullExercise(exercise.person)
            }
        }
    }

    return status
}