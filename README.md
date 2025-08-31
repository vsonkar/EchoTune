EchoTune is a lightweight and modern local music player application for Android, designed to provide users with a clean interface and smooth playback experience. Built with Java, ExoPlayer, RecyclerView, and MediaStore API, the app scans the device’s storage for audio files and allows seamless playback with essential music controls.

📂 Core Components

SplashActivity – Entry point with navigation to the main library.

MainActivity – Handles permissions, loads songs, and displays them in a list.

PlayerActivity – Manages playback with ExoPlayer, UI controls, waveform seekbar, and album art.

Song Model (Parcelable) – Represents each track with id, title, artist, data, and albumId.

SongAdapter – Binds songs to the RecyclerView with album art and click handling.

.

🚀 Outcomes

Successfully implemented a functional and visually appealing music player.

Practiced modern Android development patterns like ViewBinding, runtime permissions, and ExoPlayer integration.

Delivered a scalable codebase that can be extended with features such as playlists, favorites, or online streaming.
