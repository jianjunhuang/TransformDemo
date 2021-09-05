package xyz.juncat.transform

import com.android.build.gradle.AppExtension
import javassist.ClassPool
import javassist.CtClass
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import java.io.File

object InjectUtils {

    fun inject(path: String, project: Project) {
        val dir = File(path)
        if (dir.isDirectory) {
            listFiles(dir, path, project)
        }
    }

    private fun listFiles(file: File, path: String, project: Project) {
        if (file.isDirectory) {
            file.listFiles()?.forEach {
                listFiles(it, path, project)
            }
        } else {
            println("injectTimeLog => ${file.absolutePath}")
            if (file.name.endsWith(".class")) {
                doInjectTimeLog(project, file, path)
            }
        }
    }

    private fun doInjectTimeLog(project: Project, clsFile: File, originPath: String) {
        println("doInjectTimeLog => ${clsFile.absolutePath}")
        val cls = clsFile.absolutePath.removePrefix("$originPath/")
            .replace('/', '.')
        println("doInjectTimeLog => $cls")
        val clsName = cls.substring(0, cls.lastIndexOf(".class"))
        println("doInjectTimeLog => $clsName")

        val pool = ClassPool.getDefault()
        //add path
        pool.appendClassPath(originPath)
        //add android.jar
        pool.appendClassPath(project.extensions.getByType(AppExtension::class).bootClasspath[0].toString())
        pool.appendClassPath("android.os.Bundle")
        val ctClass = pool.getCtClass(clsName)

        if (ctClass.isFrozen) {
            ctClass.defrost()
        }

        ctClass.declaredMethods.forEach {
            println("injecting => ${it.name}")
            it.addLocalVariable("start", CtClass.longType)
            it.insertBefore("start = System.nanoTime();")

            val tag = "${it.longName} cost =>"
            it.insertAfter(
                """
                long cost = System.nanoTime() - start; 
                android.util.Log.i("MethodCost", "$tag "+cost);
            """.trimIndent()
            )
        }

        ctClass.writeFile(originPath)
        ctClass.detach()
    }
}