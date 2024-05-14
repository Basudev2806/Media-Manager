# Media Manager
Media Manager is a powerful tool for managing media files in your Android application. It provides easy-to-use methods for handling images, videos, and audio files, as well as retrieving folder information.

[![](https://jitpack.io/v/Basudev2806/Media-Manager.svg)](https://jitpack.io/#Basudev2806/Media-Manager)

# Download
To include Media Manager in your project, your project should have minSdkVersion 19 and above.

You can download a jar from GitHub's [releases](https://github.com/Basudev2806/Media-Manager/releases) page.

Or use Gradle:
Add it in your your projects root build.gradle file

```gradle
 allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}   
```

Add the dependency in the app level build.gradle file

```gradle
dependencies {
	   implementation 'com.github.Basudev2806:Media-Manager:1.0.0'
	}
```

# Usage
The Media Manager library already contains a set of classes for holding audio,video and image file data and each time a query is executed to get media files, the results always come in anyone of these classes.

**getAllMedia** :this class holds data for retrieving all media files (images and videos).

**getAllAudioContent** : this class holds data for retrieving all audio files.

**getFolderList** : this class holds data for retrieving a list of all folders containing media files along with the latest image in each folder.

**getRecentFolders** : this class holds for retrieving a list of recent folders containing media files along with the latest image in each folder.

## Getting audio files from the MediaStore

get All Media files

```java
MediaManager(context).getAllMedia();
```
get All Audio Content files

```java
MediaManager(context).getAllAudioContent();
```

get Folder List files

```java
MediaManager(context).getFolderList();
```
get Recent Folders files

```java
MediaManager(context).getRecentFolders();
```
## Media Manager Usage
Refer to the Media Manager section in the original README for detailed usage examples, including getting all media files, audio files, folder lists, and recent folders.

# Contributing
Contributions are welcome. Please feel free to submit issues or pull requests.

# License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
