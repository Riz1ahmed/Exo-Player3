package com.example.exoplayer

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import com.example.exoplayer.databinding.ActivityMainBinding
import androidx.media3.exoplayer.ExoPlayer

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val player: ExoPlayer by lazy {
        ExoPlayer.Builder(this).build()
    }

    private val videoUrls = listOf(
        "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "https://storage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
        "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
        "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
        "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
        "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
        "https://storage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
        "https://storage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
    )
    private var currentIdx = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {

                Log.d("xyz", "onPlaybackStateChanged: $playbackState")
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        Log.d("xyz", "onPlaybackStateChanged: Play loading...")
                        binding.btnPlay.text = "Loading..."
                    }

                    Player.STATE_READY -> {
                        Log.d("xyz", "onPlaybackStateChanged: Play ready")
                        binding.btnPlay.text = if (player.isPlaying) "Stop" else "Play"
                    }

                    Player.STATE_ENDED -> {
                        Log.d("xyz", "onPlaybackStateChanged: Play end")
                        binding.btnPlay.text = "Play again"
                    }

                    Player.STATE_IDLE -> {
                        Log.d("xyz", "onPlaybackStateChanged: Nothing")
                        binding.btnPlay.text = "..."
                    }
                }

                super.onPlaybackStateChanged(playbackState)
            }
        })

        binding.playerView.player = player

        updateMediaItem()

        binding.btnPlay.setOnClickListener {
            if (player.isPlaying) {
                player.playWhenReady = false
                player.seekTo(0)

                Toast.makeText(this@MainActivity, "Stop video", Toast.LENGTH_SHORT).show()

            } else {
                player.seekTo(0)
                player.playWhenReady = true

                Toast.makeText(this@MainActivity, "Play video", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnNext.setOnClickListener {
            currentIdx = ((currentIdx + 1) % videoUrls.size)
            updateMediaItem()
        }
        binding.btnPrevious.setOnClickListener {
            currentIdx = ((currentIdx - 1) % videoUrls.size)
            updateMediaItem()
        }

        controlPlayerSize()
    }

    private fun updateMediaItem() {
        val mediaItem = MediaItem.Builder()
            .setUri(videoUrls[currentIdx])
            .setMimeType(MimeTypes.APPLICATION_MP4)
            .build()

        player.apply {
            setMediaItem(mediaItem)
            seekTo(0, 0L)
            prepare()
        }
    }

    fun controlPlayerSize() {
        binding.seekRotate.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("xyz", "onProgressChanged: Rotation=$progress")
                binding.playerView.rotation = progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.seekScaleX.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.playerView.scaleX = progress / 100F
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.seekScaleY.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.playerView.scaleY = progress / 100F

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

    }
}