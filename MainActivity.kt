plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'org.jetbrains.kotlin.plugin.compose'
}

android {
    namespace 'ir.firoeu.dietrix'
    compileSdk 34

    defaultConfig {
        applicationId "ir.firoeu.dietrix"
        minSdk 26
        targetSdk 34
        versionCode 3
        versionName "2.0"
    }

    buildTypes {
        release {
            minifyEnabled false
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = '17'
    }

    buildFeatures {
        compose true
    }
}

dependencies {
    implementation platform('androidx.compose:compose-bom:2024.04.01')
    implementation 'androidx.core:core-ktx:1.13.1'
    implementation 'androidx.activity:activity-compose:1.9.0'
    implementation 'androidx.compose.ui:ui'
    implementation 'androidx.compose.foundation:foundation'
    implementation 'androidx.compose.material3:material3'
}

// Download the IRANSansDN font (from the live web app) and convert it to TTF at build time.
// The app falls back to the system font when the asset is missing.
tasks.register('fetchPersianFont') {
    doLast {
        def out = new File(projectDir, 'src/main/assets/IRANSansDN.ttf')
        if (out.exists() && out.length() > 10000) return
        try {
            out.parentFile.mkdirs()
            def css = new URL('https://raw.githubusercontent.com/firoeu-del/diet-app/main/font.css').text
            def m = css =~ /base64,([^)]+)\)/
            if (m.find()) {
                def woff = m.group(1).decodeBase64()
                def tmpW = File.createTempFile('font', '.woff')
                tmpW.bytes = woff
                def py = File.createTempFile('conv', '.py')
                py.text = 'import sys\nfrom fontTools.ttLib import TTFont\nf = TTFont(sys.argv[1])\nf.flavor = None\nf.save(sys.argv[2])\n'
                ['bash', '-c', 'pip3 install --quiet fonttools'].execute().waitFor()
                def p = ['python3', py.absolutePath, tmpW.absolutePath, out.absolutePath].execute()
                p.waitFor()
                println('fetchPersianFont: ' + (out.exists() ? 'ok, ' + out.length() + ' bytes' : 'conversion failed'))
            }
        } catch (Exception e) {
            println('fetchPersianFont: skipped (' + e + ')')
        }
    }
}
tasks.named('preBuild') { dependsOn 'fetchPersianFont' }
