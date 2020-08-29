package com.faendir.om.discord.reddit

import com.faendir.om.discord.leaderboards.Leaderboard
import com.faendir.om.discord.model.Score
import com.faendir.om.discord.puzzle.Puzzle
import com.faendir.om.discord.utils.find
import net.dean.jraw.models.SubredditSort
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
@EnableScheduling
class RedditPostScraper(private val redditService: RedditService, private val leaderboards: List<Leaderboard>) {
    private lateinit var trustedUsers: List<String>
    val reddit
        get() = redditService.reddit
    private var lastUpdate: Date? = null
    val mainRegex = Regex("\\s*(?<puzzle>[^:]*)[:\\s]\\s*(\\[[^]]*]\\([^)]*\\)[,\\s]*)+")
    val scoreRegex =
        Regex("\\[(?<score>[\\d.]+[a-zA-Z]?/[\\d.]+[a-zA-Z]?/[\\d.]+[a-zA-Z]?(/[\\d.]+[a-zA-Z]?)?)]\\((?<link>http.*)\\)[,\\s]*")

    init {
        redditService.accessRepo { _, repo ->
            trustedUsers = File(repo, "trusted_users.txt").readLines().filter { !it.isBlank() }.map { it.trim() }
        }
    }

    /*@PostConstruct
    fun importOldStuff() {
        val regex =
            Regex("!submit\\s+(?<puzzle>[^:]*)(:|\\s)\\s*(?<score>[\\d.]+[a-zA-Z]?/[\\d.]+[a-zA-Z]?/[\\d.]+[a-zA-Z]?(/[\\d.]+[a-zA-Z]?)?)(\\s+(?<link>http.*\\.(gif|gifv|mp4|webm)))?\\s*")
        redditService.accessRepo { _, repo ->
            File(repo, "commands.txt").readLines().forEach loop@{ line ->
                val command = regex.matchEntire(line.let {
                    if (!it.endsWith(".gif") && !it.endsWith(".gifv") && !it.endsWith(".mp4") && !it.endsWith(".webm")) {
                        "$it.gif"
                    } else it
                }) ?: return@loop
                val puzzleName = command.groups["puzzle"]!!.value
                val puzzles = Puzzle.findByName(puzzleName)
                if (puzzles.isEmpty() || puzzles.size > 1) {
                    return@loop
                }
                val puzzle = puzzles.first()
                val score: Score = Score.parse(puzzle, command.groups["score"]!!.value) ?: return@loop
                val leaderboardCategories = leaderboards.find(puzzle, score)
                if (leaderboardCategories.isEmpty()) {
                    return@loop
                }
                val link = command.groups["link"]!!.value
                leaderboardCategories.forEach { (leaderboard, categories) ->
                    leaderboard.update(
                        "nobody",
                        puzzle,
                        categories,
                        score,
                        link
                    )
                }
            }
        }
    }*/

    @Scheduled(fixedRate = 1000 * 60 * 60)
    fun pullFromReddit() {
        val submissionThread =
            reddit.subreddit("opus_magnum").posts().sorting(SubredditSort.HOT).limit(5).build().asSequence().flatten()
                .find { it.title.contains("official record submission thread", ignoreCase = true) }
        submissionThread?.toReference(reddit)?.comments()?.walkTree()?.forEach { commentNode ->
            val comment = commentNode.subject
            if (comment.body != null && comment.body != "[deleted]" && trustedUsers.contains(comment.author)) {
                if (lastUpdate == null || (comment.edited ?: comment.created).after(lastUpdate)) {
                    comment.body!!.lines().forEach loop@{ line ->
                        val command = mainRegex.matchEntire(line) ?: return@loop
                        val puzzleName = command.groups["puzzle"]!!.value
                        val puzzles = Puzzle.findByName(puzzleName)
                        if (puzzles.isEmpty() || puzzles.size > 1) {
                            return@loop
                        }
                        val puzzle = puzzles.first()
                        command.groupValues.drop(2).forEach inner@{ group ->
                            val subCommand = scoreRegex.matchEntire(group) ?: return@inner
                            val score: Score = Score.parse(puzzle, subCommand.groups["score"]!!.value) ?: return@inner
                            val leaderboardCategories = leaderboards.find(puzzle, score)
                            if (leaderboardCategories.isEmpty()) {
                                return@inner
                            }
                            val link = subCommand.groups["link"]!!.value
                            leaderboardCategories.forEach { (leaderboard, categories) ->
                                leaderboard.update(
                                    comment.author,
                                    puzzle,
                                    categories,
                                    score,
                                    link
                                )
                            }
                        }
                    }
                }
            }
        }
        lastUpdate = Date.from(Instant.now().minus(5, ChronoUnit.MINUTES))
    }
}