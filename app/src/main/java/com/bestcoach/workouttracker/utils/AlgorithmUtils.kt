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
    NO_EXERCISE, PUSH, PULL, PLANK, SIDE_PLANK, PIKE_PRESS_UP, WALL_SIT, STRAIGHT_BRIDGE, NINTY_DEGREE_STATIC_PRESS
}

private const val ERROR_MARGIN: Double = 25.0

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

    if ((Right_angle_push >= (135 + ERROR_MARGIN)) || (Left_angle_push >= (135 + ERROR_MARGIN))) {
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

fun plankExercise(person: Person): String {
    currentExercise = Exercises.PLANK

    val leftKnee: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_KNEE
    }

    val rightKnee: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_KNEE
    }

    val leftHip: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_HIP
    }

    val rightHip: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_HIP
    }

    val leftShoulder: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_SHOULDER
    }

    val rightShoulder: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_SHOULDER
    }

    val R_hip_R_knee_x = rightHip!!.position.x - rightKnee!!.position.x

    val R_hip_R_knee_y = rightHip.position.y - rightKnee.position.y

    val R_hip_R_shoulder_x = rightHip.position.x - rightShoulder!!.position.x

    val R_hip_R_shoulder_y = rightHip.position.y - rightShoulder.position.y

    var Right_angle_plank = atan2(R_hip_R_knee_y.toDouble(), R_hip_R_knee_x.toDouble()) -
            atan2(R_hip_R_shoulder_y.toDouble(), R_hip_R_shoulder_x.toDouble())

    val L_hip_L_knee_x = leftHip!!.position.x - leftKnee!!.position.x

    val L_hip_L_knee_y = leftHip.position.y - leftKnee.position.y

    val L_hip_L_shoulder_x = leftHip.position.x - leftShoulder!!.position.x

    val L_hip_L_shoulder_y = leftHip.position.y - leftShoulder.position.y

    var Left_angle_plank = atan2(L_hip_L_shoulder_y.toDouble(), L_hip_L_shoulder_x.toDouble()) -
            atan2(L_hip_L_knee_y.toDouble(), L_hip_L_knee_x.toDouble())

    Left_angle_plank = abs(Left_angle_plank * (180 / kotlin.math.PI))

    Right_angle_plank = abs(Right_angle_plank * (180 / kotlin.math.PI))

    if (Right_angle_plank > (180 + ERROR_MARGIN) || Left_angle_plank > (180 + ERROR_MARGIN)) {
        correctFlag = 0
        return "Raise your HIP"

    } else if (Right_angle_plank < (180 - ERROR_MARGIN) || Left_angle_plank < (180 - ERROR_MARGIN)) {
        correctFlag = 0
        return "Lower your HIP"

    } else if (correctFlag == 0) {
        correctFlag = 1
        return "You are correct"

    } else {
        return ""
    }
}


fun sidePlankExercise(person: Person): String {
    currentExercise = Exercises.SIDE_PLANK

    val leftHip: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_HIP
    }

    val rightHip: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_HIP
    }

    val leftShoulder: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_SHOULDER
    }

    val rightShoulder: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_SHOULDER
    }

    val leftKnee: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_KNEE
    }

    val rightKnee: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_KNEE
    }

    val R_hip_R_knee_x = rightHip!!.position.x - rightKnee!!.position.x

    val R_hip_R_knee_y = rightHip.position.y - rightKnee.position.y

    val R_hip_R_shoulder_x = rightHip.position.x - rightShoulder!!.position.x

    val R_hip_R_shoulder_y = rightHip.position.y - rightShoulder.position.y

    var Right_angle_side_plank = atan2(R_hip_R_knee_y.toDouble(), R_hip_R_knee_x.toDouble()) -
            atan2(R_hip_R_shoulder_y.toDouble(), R_hip_R_shoulder_x.toDouble())

    val L_hip_L_knee_x = leftHip!!.position.x - leftKnee!!.position.x

    val L_hip_L_knee_y = leftHip.position.y - leftKnee.position.y

    val L_hip_L_shoulder_x = leftHip.position.x - leftShoulder!!.position.x

    val L_hip_L_shoulder_y = leftHip.position.y - leftShoulder.position.y

    var Left_angle_side_plank =
            atan2(L_hip_L_shoulder_y.toDouble(), L_hip_L_shoulder_x.toDouble()) -
                    atan2(L_hip_L_knee_y.toDouble(), L_hip_L_knee_x.toDouble())

    Left_angle_side_plank = abs(Left_angle_side_plank * (180 / kotlin.math.PI))

    Right_angle_side_plank = abs(Right_angle_side_plank * (180 / kotlin.math.PI))
    
    if (Right_angle_side_plank > (180 + ERROR_MARGIN) || Left_angle_side_plank > (180 + ERROR_MARGIN)) {
        correctFlag = 0
        return "Raise your WAIST"

    } else if (Right_angle_side_plank < (180 - ERROR_MARGIN) || Left_angle_side_plank < (180 - ERROR_MARGIN)) {
        correctFlag = 0
        return "Lower your WAIST"

    } else if (correctFlag == 0) {
        correctFlag = 1
        return "You are correct"

    } else {
        return ""
    }
}

fun pikePressUpExercise(person: Person): String {
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

    val rightElbow: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_ELBOW
    }

    val leftElbow: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_ELBOW
    }

    val R_hip_R_knee_x = rightHip!!.position.x - rightKnee!!.position.x

    val R_hip_R_knee_y = rightHip.position.y - rightKnee.position.y

    val R_hip_R_elbow_x = rightHip.position.x - rightElbow!!.position.x

    val R_hip_R_elbow_y = rightHip.position.y - rightElbow.position.y

    var Right_angle_pike_press_up = atan2(R_hip_R_knee_y.toDouble(), R_hip_R_knee_x.toDouble()) -
            atan2(R_hip_R_elbow_y.toDouble(), R_hip_R_elbow_x.toDouble())

    val L_hip_L_knee_x = leftHip!!.position.x - leftKnee!!.position.x

    val L_hip_L_knee_y = leftHip.position.y - leftKnee.position.y

    val L_hip_L_elbow_x = leftHip.position.x - leftElbow!!.position.x

    val L_hip_L_elbow_y = leftHip.position.y - leftElbow.position.y

    var Left_angle_pike_press_up =
        atan2(L_hip_L_elbow_y.toDouble(), L_hip_L_elbow_x.toDouble()) -
                atan2(L_hip_L_knee_y.toDouble(), L_hip_L_knee_x.toDouble())

    Left_angle_pike_press_up = abs(Left_angle_pike_press_up * (180 / kotlin.math.PI))
    Right_angle_pike_press_up = abs(Right_angle_pike_press_up * (180 / kotlin.math.PI))

    if (Left_angle_pike_press_up > 180) {
        Left_angle_pike_press_up = 360 - Left_angle_pike_press_up
    }

    if (Right_angle_pike_press_up > 180) {
        Right_angle_pike_press_up = 360 - Right_angle_pike_press_up
    }

    if (Right_angle_pike_press_up > (90 + ERROR_MARGIN) || Left_angle_pike_press_up > (90 + ERROR_MARGIN)) {
        correctFlag = 0
        return "Raise your HIP"

    } else if (Right_angle_pike_press_up < (90 - ERROR_MARGIN) || Left_angle_pike_press_up < (90 - ERROR_MARGIN)) {
        correctFlag = 0
        return "Lower your HIP"

    } else if (correctFlag == 0) {
        correctFlag = 1
        return "You are correct"

    } else {
        return ""
    }

}

fun wallSitExercise(person: Person): String {
    currentExercise = Exercises.WALL_SIT

    val leftHip: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_HIP
    }

    val rightHip: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_HIP
    }

    val leftShoulder: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_SHOULDER
    }

    val rightShoulder: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_SHOULDER
    }

    val leftKnee: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_KNEE
    }

    val rightKnee: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_KNEE
    }

    val R_hip_R_knee_x = rightHip!!.position.x - rightKnee!!.position.x

    val R_hip_R_knee_y = rightHip.position.y - rightKnee.position.y

    val R_hip_R_shoulder_x = rightHip.position.x - rightShoulder!!.position.x

    val R_hip_R_shoulder_y = rightHip.position.y - rightShoulder.position.y

    var Right_angle_wall_sit = atan2(R_hip_R_knee_y.toDouble(), R_hip_R_knee_x.toDouble()) -
            atan2(R_hip_R_shoulder_y.toDouble(), R_hip_R_shoulder_x.toDouble())

    val L_hip_L_knee_x = leftHip!!.position.x - leftKnee!!.position.x

    val L_hip_L_knee_y = leftHip.position.y - leftKnee.position.y

    val L_hip_L_shoulder_x = leftHip.position.x - leftShoulder!!.position.x

    val L_hip_L_shoulder_y = leftHip.position.y - leftShoulder.position.y

    var Left_angle_wall_sit =
        atan2(L_hip_L_shoulder_y.toDouble(), L_hip_L_shoulder_x.toDouble()) -
                atan2(L_hip_L_knee_y.toDouble(), L_hip_L_knee_x.toDouble())

    Left_angle_wall_sit = abs(Left_angle_wall_sit * (180 / kotlin.math.PI))
    Right_angle_wall_sit = abs(Right_angle_wall_sit * (180 / kotlin.math.PI))

    if (Left_angle_wall_sit > 180) {
        Left_angle_wall_sit = 360 - Left_angle_wall_sit
    }

    if (Right_angle_wall_sit > 180) {
        Right_angle_wall_sit = 360 - Right_angle_wall_sit
    }

    if (Right_angle_wall_sit > (90 + ERROR_MARGIN / 2) || Left_angle_wall_sit > (90 + ERROR_MARGIN / 2)) {
        correctFlag = 0
        return "Lower your HIP"

    } else if (Right_angle_wall_sit < (90 - ERROR_MARGIN / 2) || Left_angle_wall_sit < (90 - ERROR_MARGIN / 2)) {
        correctFlag = 0
        return "Raise your HIP"

    } else if (correctFlag == 0) {
        correctFlag = 1
        return "You are correct"

    } else {
        return ""
    }
}

fun straightBridgeExercise(person: Person): String {
    currentExercise = Exercises.STRAIGHT_BRIDGE

    val leftKnee: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_KNEE
    }

    val rightKnee: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_KNEE
    }

    val leftHip: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_HIP
    }

    val rightHip: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_HIP
    }

    val leftShoulder: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_SHOULDER
    }

    val rightShoulder: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_SHOULDER
    }

    val R_hip_R_knee_x = rightHip!!.position.x - rightKnee!!.position.x

    val R_hip_R_knee_y = rightHip.position.y - rightKnee.position.y

    val R_hip_R_shoulder_x = rightHip.position.x - rightShoulder!!.position.x

    val R_hip_R_shoulder_y = rightHip.position.y - rightShoulder.position.y

    var Right_angle_straight_bridge = atan2(R_hip_R_knee_y.toDouble(), R_hip_R_knee_x.toDouble()) -
            atan2(R_hip_R_shoulder_y.toDouble(), R_hip_R_shoulder_x.toDouble())

    val L_hip_L_knee_x = leftHip!!.position.x - leftKnee!!.position.x

    val L_hip_L_knee_y = leftHip.position.y - leftKnee.position.y

    val L_hip_L_shoulder_x = leftHip.position.x - leftShoulder!!.position.x

    val L_hip_L_shoulder_y = leftHip.position.y - leftShoulder.position.y

    var Left_angle_straight_bridge = atan2(L_hip_L_shoulder_y.toDouble(), L_hip_L_shoulder_x.toDouble()) -
            atan2(L_hip_L_knee_y.toDouble(), L_hip_L_knee_x.toDouble())

    Left_angle_straight_bridge = abs(Left_angle_straight_bridge * (180 / kotlin.math.PI))
    Right_angle_straight_bridge = abs(Right_angle_straight_bridge * (180 / kotlin.math.PI))

    if (Left_angle_straight_bridge > 180) {
        Left_angle_straight_bridge = 360 - Left_angle_straight_bridge
    }

    if (Right_angle_straight_bridge > 180) {
        Right_angle_straight_bridge = 360 - Right_angle_straight_bridge
    }

    if (Right_angle_straight_bridge > (180 + ERROR_MARGIN) || Left_angle_straight_bridge > (180 + ERROR_MARGIN)) {
        correctFlag = 0
        return "Raise your HIP"

    } else if (Right_angle_straight_bridge < (180 - ERROR_MARGIN) || Left_angle_straight_bridge < (180 - ERROR_MARGIN)) {
        correctFlag = 0
        return "Lower your HIP"

    } else if (correctFlag == 0) {
        correctFlag = 1
        return "You are correct"

    } else {
        return ""
    }
}

fun nintyDegreeStaticPressExercise(person: Person): String {
    currentExercise = Exercises.NINTY_DEGREE_STATIC_PRESS

    val leftHip: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_HIP
    }

    val rightHip: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_HIP
    }

    val leftShoulder: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_SHOULDER
    }

    val rightShoulder: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_SHOULDER
    }

    val leftKnee: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.LEFT_KNEE
    }

    val rightKnee: KeyPoint? = person.keyPoints.find {
        it.bodyPart == BodyPart.RIGHT_KNEE
    }

    val R_hip_R_knee_x = rightHip!!.position.x - rightKnee!!.position.x

    val R_hip_R_knee_y = rightHip.position.y - rightKnee.position.y

    val R_hip_R_shoulder_x = rightHip.position.x - rightShoulder!!.position.x

    val R_hip_R_shoulder_y = rightHip.position.y - rightShoulder.position.y

    var Right_angle_90_degree_static_press = atan2(R_hip_R_knee_y.toDouble(), R_hip_R_knee_x.toDouble()) -
            atan2(R_hip_R_shoulder_y.toDouble(), R_hip_R_shoulder_x.toDouble())

    val L_hip_L_knee_x = leftHip!!.position.x - leftKnee!!.position.x

    val L_hip_L_knee_y = leftHip.position.y - leftKnee.position.y

    val L_hip_L_shoulder_x = leftHip.position.x - leftShoulder!!.position.x

    val L_hip_L_shoulder_y = leftHip.position.y - leftShoulder.position.y

    var Left_angle_90_degree_static_press =
        atan2(L_hip_L_shoulder_y.toDouble(), L_hip_L_shoulder_x.toDouble()) -
                atan2(L_hip_L_knee_y.toDouble(), L_hip_L_knee_x.toDouble())

    Left_angle_90_degree_static_press = abs(Left_angle_90_degree_static_press * (180 / kotlin.math.PI))
    Right_angle_90_degree_static_press = abs(Right_angle_90_degree_static_press * (180 / kotlin.math.PI))

    if (Left_angle_90_degree_static_press > 180) {
        Left_angle_90_degree_static_press = 360 - Left_angle_90_degree_static_press
    }

    if (Right_angle_90_degree_static_press > 180) {
        Right_angle_90_degree_static_press = 360 - Right_angle_90_degree_static_press
    }

    if (Right_angle_90_degree_static_press > (90 + ERROR_MARGIN ) || Left_angle_90_degree_static_press > (90 + ERROR_MARGIN )) {
        correctFlag = 0
        return "Move your knees closer to your chest"

    } else if (Right_angle_90_degree_static_press < (90 - ERROR_MARGIN ) || Left_angle_90_degree_static_press < (90 - ERROR_MARGIN)) {
        correctFlag = 0
        return "Move your knees away from your chest"

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

            context.getString(R.string.PLANK) -> {
                if (currentExercise != Exercises.PLANK) correctFlag = 0
                status = plankExercise(exercise.person)
            }

            context.getString(R.string.SIDE_PLANK) -> {
                if (currentExercise != Exercises.SIDE_PLANK) correctFlag = 0
                status = sidePlankExercise(exercise.person)
            }

            context.getString(R.string.PIKE_PRESS_UP) -> {
                if (currentExercise != Exercises.PIKE_PRESS_UP) correctFlag = 0
                status = pikePressUpExercise(exercise.person)
            }

            context.getString(R.string.WALL_SIT) -> {
                if (currentExercise != Exercises.WALL_SIT) correctFlag = 0
                status = wallSitExercise(exercise.person)
            }

            context.getString(R.string.STRAIGHT_BRIDGE) -> {
                if (currentExercise != Exercises.STRAIGHT_BRIDGE) correctFlag = 0
                status = straightBridgeExercise(exercise.person)
            }

            context.getString(R.string.NINETY_DEGREE_STATIC_PRESS) -> {
                if (currentExercise != Exercises.NINTY_DEGREE_STATIC_PRESS) correctFlag = 0
                status = nintyDegreeStaticPressExercise(exercise.person)
            }

        }
    }
    return status
}