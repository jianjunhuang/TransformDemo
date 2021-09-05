package xyz.juncat.transform

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

class MethodTimeCostTransform(private val project: Project) : Transform() {
    override fun getName(): String {
        return "MethodTimeCostTransform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return mutableSetOf(
            QualifiedContent.DefaultContentType.CLASSES
        )
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return mutableSetOf(
            QualifiedContent.Scope.PROJECT,
            QualifiedContent.Scope.SUB_PROJECTS
        )
    }

    override fun isIncremental(): Boolean {
        return true
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)

        transformInvocation?.inputs?.forEach { input ->
            //手写的 Class 类 & R.class, BuildConfig.class ...
            input.directoryInputs.forEach { dirInput ->
                val path = dirInput.file.absolutePath
                println("[$name] -----> begin inject: $path")
                InjectUtils.inject(path, project)
                //输出目录
                val dest = transformInvocation.outputProvider.getContentLocation(
                    dirInput.name,
                    dirInput.contentTypes,
                    dirInput.scopes,
                    Format.DIRECTORY
                )

                println("[$name] -----> output: $dest")
                FileUtils.copyDirectory(dirInput.file, dest)
            }

            input.jarInputs.forEach {
                val dest = transformInvocation.outputProvider.getContentLocation(
                    it.name,
                    it.contentTypes,
                    it.scopes,
                    Format.JAR
                )
                FileUtils.copyFile(it.file, dest)
            }
        }
    }

}