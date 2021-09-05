package xyz.juncat.transform

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class TransformPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        val methodTimeTransform = MethodTimeCostTransform(target)

        //AppExtension => android {}
        target.extensions.getByType(AppExtension::class).let { ext ->
            ext.registerTransform(methodTimeTransform)
        }
    }
}