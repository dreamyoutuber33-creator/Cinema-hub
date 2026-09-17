package com.example.data.repository

import com.example.data.model.CastMember
import com.example.data.model.Movie
import com.example.data.model.VideoResolutionSource

object LegalMoviesDataSource {

    val movies: List<Movie> = listOf(
        Movie(
            id = "tos_2012",
            title = "Tears of Steel",
            overview = "In a dystopian future set in Amsterdam, a group of warriors and scientists attempt to stage a crucial event in the past in a desperate bid to rescue the world from destructive robotic tentacles.",
            posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cb/Tears_of_Steel_poster.jpg/800px-Tears_of_Steel_poster.jpg",
            backdropUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?auto=format&fit=crop&w=1200&q=80",
            releaseYear = 2012,
            releaseDate = "September 26, 2012",
            rating = 8.4,
            voteCount = 4210,
            runtimeMinutes = 12,
            genres = listOf("Sci-Fi", "Action", "VFX"),
            resolutionBadge = "4K UHD",
            language = "English",
            country = "Netherlands",
            director = "Ian Hubert",
            cast = listOf(
                CastMember("Derek de Lint", "Old Thom", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200"),
                CastMember("Vanja Rukavina", "Thom", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200"),
                CastMember("Denise Rebergen", "Celia", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200"),
                CastMember("Jodie de Beijer", "Barley", "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200")
            ),
            production = "Blender Foundation & Blender Institute",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            videoSources = listOf(
                VideoResolutionSource("Auto (Adaptive)", 3840, 2160, 14000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"),
                VideoResolutionSource("2160p 4K UHD", 3840, 2160, 14000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"),
                VideoResolutionSource("1080p Full HD", 1920, 1080, 6000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"),
                VideoResolutionSource("720p HD", 1280, 720, 3000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"),
                VideoResolutionSource("480p SD", 854, 480, 1500000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"),
                VideoResolutionSource("360p Low", 640, 360, 800000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4")
            ),
            isFeatured = true
        ),
        Movie(
            id = "sintel_2010",
            title = "Sintel",
            overview = "A lonely young woman named Sintel rescues and nurses a wounded baby dragon, forming an unbreakable bond. When an adult dragon swoops down and kidnaps her companion, Sintel embarks on a treacherous quest across barren tundras, mountain peaks, and ancient temples to bring him home.",
            posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/Sintel_poster.jpg/800px-Sintel_poster.jpg",
            backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=1200&q=80",
            releaseYear = 2010,
            releaseDate = "September 27, 2010",
            rating = 8.8,
            voteCount = 8950,
            runtimeMinutes = 15,
            genres = listOf("Animation", "Fantasy", "Adventure", "Drama"),
            resolutionBadge = "4K HDR",
            language = "English",
            country = "Netherlands",
            director = "Colin Levy",
            cast = listOf(
                CastMember("Halina Reijn", "Sintel (voice)", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200"),
                CastMember("Thom Hoffman", "Shaman (voice)", "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200")
            ),
            production = "Blender Foundation",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            videoSources = listOf(
                VideoResolutionSource("Auto (Adaptive)", 1920, 1080, 5000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"),
                VideoResolutionSource("1080p Full HD", 1920, 1080, 5000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"),
                VideoResolutionSource("720p HD", 1280, 720, 2500000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"),
                VideoResolutionSource("480p SD", 854, 480, 1200000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"),
                VideoResolutionSource("360p Low", 640, 360, 700000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4")
            ),
            isFeatured = true
        ),
        Movie(
            id = "bbb_2008",
            title = "Big Buck Bunny",
            overview = "A gigantic, warm-hearted rabbit with a gentle soul wakes up on a sunny spring morning only to have his peaceful forest day disturbed by a bullying trio of mischievous rodents: Frank, Rinky, and Gamera. Bunny decides to engineer poetic and hilarious revenge.",
            posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Big_buck_bunny_poster_big.jpg/800px-Big_buck_bunny_poster_big.jpg",
            backdropUrl = "https://images.unsplash.com/photo-1511447333015-45b65e60f6d5?auto=format&fit=crop&w=1200&q=80",
            releaseYear = 2008,
            releaseDate = "April 10, 2008",
            rating = 8.1,
            voteCount = 12040,
            runtimeMinutes = 10,
            genres = listOf("Animation", "Comedy", "Family"),
            resolutionBadge = "4K 60fps",
            language = "English",
            country = "Netherlands",
            director = "Sacha Goedegebure",
            cast = listOf(
                CastMember("Big Buck Bunny", "Himself", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200"),
                CastMember("Frank the Squirrel", "Antagonist", "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=200")
            ),
            production = "Blender Institute",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            videoSources = listOf(
                VideoResolutionSource("Auto (Adaptive)", 3840, 2160, 12000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
                VideoResolutionSource("2160p 4K UHD", 3840, 2160, 12000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
                VideoResolutionSource("1080p Full HD", 1920, 1080, 5500000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
                VideoResolutionSource("720p HD", 1280, 720, 2500000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
                VideoResolutionSource("480p SD", 854, 480, 1200000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
            ),
            isFeatured = true
        ),
        Movie(
            id = "cosmos_2015",
            title = "Cosmos Laundromat",
            overview = "On a desolate windswept island, a suicidal sheep named Franck meets a mysterious salesman named Victor, who offers him the gift of a lifetime: a washing machine that transports him into parallel universes and wildly divergent bodily forms.",
            posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cf/Cosmos_Laundromat_Poster.jpg/800px-Cosmos_Laundromat_Poster.jpg",
            backdropUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=1200&q=80",
            releaseYear = 2015,
            releaseDate = "August 10, 2015",
            rating = 8.6,
            voteCount = 3800,
            runtimeMinutes = 12,
            genres = listOf("Sci-Fi", "Comedy", "Drama", "Animation"),
            resolutionBadge = "4K UHD",
            language = "English",
            country = "Netherlands",
            director = "Mathieu Auvray",
            cast = listOf(
                CastMember("Pierre Bokma", "Franck (voice)", "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=200"),
                CastMember("Reinout Scholten van Aschat", "Victor (voice)", "https://images.unsplash.com/photo-1501196354995-cbb51c65aaea?w=200")
            ),
            production = "Blender Animation Studio",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            videoSources = listOf(
                VideoResolutionSource("Auto (Adaptive)", 3840, 2160, 13500000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
                VideoResolutionSource("2160p 4K UHD", 3840, 2160, 13500000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
                VideoResolutionSource("1080p Full HD", 1920, 1080, 5500000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
                VideoResolutionSource("720p HD", 1280, 720, 2600000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
            ),
            isFeatured = true
        ),
        Movie(
            id = "elephants_dream_2006",
            title = "Elephants Dream",
            overview = "Emo and Proog, two individuals navigating the surreal, mechanical labyrinth of an enormous and mysterious machine, explore their conflicting realities and perceptions of reality.",
            posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/Elephants_Dream_poster.jpg/800px-Elephants_Dream_poster.jpg",
            backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=1200&q=80",
            releaseYear = 2006,
            releaseDate = "March 24, 2006",
            rating = 7.9,
            voteCount = 2890,
            runtimeMinutes = 11,
            genres = listOf("Sci-Fi", "Animation", "Cyberpunk"),
            resolutionBadge = "1080p FHD",
            language = "English",
            country = "Netherlands",
            director = "Bassam Kurdali",
            cast = listOf(
                CastMember("Tygo Gernandt", "Proog (voice)", "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=200"),
                CastMember("Cas Jansen", "Emo (voice)", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200")
            ),
            production = "Orange Open Movie Project",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            videoSources = listOf(
                VideoResolutionSource("Auto (Adaptive)", 1920, 1080, 4800000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
                VideoResolutionSource("1080p Full HD", 1920, 1080, 4800000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
                VideoResolutionSource("720p HD", 1280, 720, 2400000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
                VideoResolutionSource("480p SD", 854, 480, 1100000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
            ),
            isFeatured = false
        ),
        Movie(
            id = "spring_2019",
            title = "Spring",
            overview = "A shepherd girl and her loyal dog face ancient spirits in order to continue the cycle of life. This poetic, visually stunning story tells of transformation and the arrival of Spring.",
            posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Spring_-_Blender_Open_Movie.webm/800px--Spring_-_Blender_Open_Movie.webm.jpg",
            backdropUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=1200&q=80",
            releaseYear = 2019,
            releaseDate = "April 4, 2019",
            rating = 8.7,
            voteCount = 5120,
            runtimeMinutes = 8,
            genres = listOf("Animation", "Fantasy", "Nature"),
            resolutionBadge = "4K UHD",
            language = "English",
            country = "Netherlands",
            director = "Andy Goralczyk",
            cast = listOf(
                CastMember("Spring Maiden", "Heroine", "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=200"),
                CastMember("Loyal Companion", "Faithful Dog", "https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=200")
            ),
            production = "Blender Studio",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            videoSources = listOf(
                VideoResolutionSource("Auto (Adaptive)", 3840, 2160, 12000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
                VideoResolutionSource("2160p 4K UHD", 3840, 2160, 12000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
                VideoResolutionSource("1080p Full HD", 1920, 1080, 5200000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
                VideoResolutionSource("720p HD", 1280, 720, 2400000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4")
            ),
            isFeatured = false
        ),
        Movie(
            id = "charge_2022",
            title = "Charge",
            overview = "In an old battery factory, an elderly security guard and a desperate thief enter into an intense, high-voltage clash over energy cells in a dystopian cyberpunk world.",
            posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Charge_-_Blender_Open_Movie.webm/800px--Charge_-_Blender_Open_Movie.webm.jpg",
            backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=1200&q=80",
            releaseYear = 2022,
            releaseDate = "December 15, 2022",
            rating = 8.5,
            voteCount = 3100,
            runtimeMinutes = 4,
            genres = listOf("Action", "Sci-Fi", "Cyberpunk"),
            resolutionBadge = "4K 60fps",
            language = "English",
            country = "Netherlands",
            director = "Hjalti Hjalmarsson",
            cast = listOf(
                CastMember("The Guard", "Veteran Defender", "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200"),
                CastMember("The Intruder", "Rogue Infiltrator", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200")
            ),
            production = "Blender Studio",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            videoSources = listOf(
                VideoResolutionSource("Auto (Adaptive)", 3840, 2160, 15000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"),
                VideoResolutionSource("2160p 4K UHD", 3840, 2160, 15000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"),
                VideoResolutionSource("1080p Full HD", 1920, 1080, 6000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"),
                VideoResolutionSource("720p HD", 1280, 720, 2800000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4")
            ),
            isFeatured = false
        ),
        Movie(
            id = "notld_1968",
            title = "Night of the Living Dead",
            overview = "A disparate group of individuals takes refuge in an abandoned farmhouse when corpses mysteriously begin returning to life and seeking human flesh. George A. Romero's seminal public-domain horror masterpiece.",
            posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/07/Night_of_the_Living_Dead_%281968%29_poster.jpg/800px-Night_of_the_Living_Dead_%281968%29_poster.jpg",
            backdropUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=1200&q=80",
            releaseYear = 1968,
            releaseDate = "October 1, 1968",
            rating = 8.2,
            voteCount = 15800,
            runtimeMinutes = 96,
            genres = listOf("Horror", "Thriller", "Mystery", "Classics"),
            resolutionBadge = "1080p Remastered",
            language = "English",
            country = "United States",
            director = "George A. Romero",
            cast = listOf(
                CastMember("Duane Jones", "Ben", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200"),
                CastMember("Judith O'Dea", "Barbra", "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200"),
                CastMember("Karl Hardman", "Harry Cooper", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200")
            ),
            production = "Image Ten (Public Domain)",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            videoSources = listOf(
                VideoResolutionSource("Auto (Adaptive)", 1920, 1080, 4200000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"),
                VideoResolutionSource("1080p Full HD", 1920, 1080, 4200000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"),
                VideoResolutionSource("720p HD", 1280, 720, 2100000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"),
                VideoResolutionSource("480p SD", 854, 480, 1000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4")
            ),
            isFeatured = false
        ),
        Movie(
            id = "charade_1963",
            title = "Charade",
            overview = "Regina Lampert falls for the dashing Peter Joshua while on a skiing holiday in the French Alps. Upon returning to Paris, she discovers her husband has been murdered and three sinister men are hounding her for a missing fortune.",
            posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Charade_1963_theatrical_poster.jpg/800px-Charade_1963_theatrical_poster.jpg",
            backdropUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?auto=format&fit=crop&w=1200&q=80",
            releaseYear = 1963,
            releaseDate = "December 5, 1963",
            rating = 8.5,
            voteCount = 14200,
            runtimeMinutes = 113,
            genres = listOf("Mystery", "Romance", "Comedy", "Thriller", "Classics"),
            resolutionBadge = "1080p FHD",
            language = "English",
            country = "United States",
            director = "Stanley Donen",
            cast = listOf(
                CastMember("Cary Grant", "Peter Joshua", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200"),
                CastMember("Audrey Hepburn", "Regina Lampert", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200"),
                CastMember("Walter Matthau", "Hamilton Bartholomew", "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200")
            ),
            production = "Stanley Donen Productions (Public Domain)",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
            videoSources = listOf(
                VideoResolutionSource("Auto (Adaptive)", 1920, 1080, 4500000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4"),
                VideoResolutionSource("1080p Full HD", 1920, 1080, 4500000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4"),
                VideoResolutionSource("720p HD", 1280, 720, 2200000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4")
            ),
            isFeatured = false
        ),
        Movie(
            id = "his_girl_friday_1940",
            title = "His Girl Friday",
            overview = "A hard-charging newspaper editor uses every trick in the book to keep his top reporter ex-wife from remarrying and leaving the news business during the coverage of an impending execution.",
            posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/08/His_Girl_Friday_poster.jpg/800px-His_Girl_Friday_poster.jpg",
            backdropUrl = "https://images.unsplash.com/photo-1485846234645-a62644f84728?auto=format&fit=crop&w=1200&q=80",
            releaseYear = 1940,
            releaseDate = "January 18, 1940",
            rating = 8.3,
            voteCount = 11200,
            runtimeMinutes = 92,
            genres = listOf("Comedy", "Romance", "Drama", "Classics"),
            resolutionBadge = "1080p FHD",
            language = "English",
            country = "United States",
            director = "Howard Hawks",
            cast = listOf(
                CastMember("Cary Grant", "Walter Burns", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200"),
                CastMember("Rosalind Russell", "Hildy Johnson", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200")
            ),
            production = "Columbia Pictures (Public Domain)",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
            videoSources = listOf(
                VideoResolutionSource("Auto (Adaptive)", 1920, 1080, 4200000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4"),
                VideoResolutionSource("1080p Full HD", 1920, 1080, 4200000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4"),
                VideoResolutionSource("720p HD", 1280, 720, 2000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4")
            ),
            isFeatured = false
        ),
        Movie(
            id = "general_1926",
            title = "The General",
            overview = "When Union spies steal an engineer's beloved locomotive with his sweetheart aboard, he single-handedly pursues it through enemy lines in one of silent cinema's greatest comedic masterworks.",
            posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/The_General_1926_theatrical_poster.jpg/800px-The_General_1926_theatrical_poster.jpg",
            backdropUrl = "https://images.unsplash.com/photo-1474487548417-781cb71495f3?auto=format&fit=crop&w=1200&q=80",
            releaseYear = 1926,
            releaseDate = "December 31, 1926",
            rating = 8.7,
            voteCount = 18900,
            runtimeMinutes = 78,
            genres = listOf("Action", "Comedy", "Adventure", "Classics"),
            resolutionBadge = "1080p Restored",
            language = "Silent (Music Score)",
            country = "United States",
            director = "Buster Keaton & Clyde Bruckman",
            cast = listOf(
                CastMember("Buster Keaton", "Johnnie Gray", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200"),
                CastMember("Marion Mack", "Annabelle Lee", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200")
            ),
            production = "Buster Keaton Productions (Public Domain)",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            videoSources = listOf(
                VideoResolutionSource("Auto (Adaptive)", 1920, 1080, 4200000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"),
                VideoResolutionSource("1080p Full HD", 1920, 1080, 4200000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"),
                VideoResolutionSource("720p HD", 1280, 720, 2000000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4")
            ),
            isFeatured = false
        ),
        Movie(
            id = "plan9_1959",
            title = "Plan 9 from Outer Space",
            overview = "Extraterrestrial beings implement 'Plan 9'—resurrecting the dead as zombies and ghouls to stop humanity from creating a universe-threatening solar-powered explosive.",
            posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cf/Plan_Nine_from_Outer_Space_poster.jpg/800px-Plan_Nine_from_Outer_Space_poster.jpg",
            backdropUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=1200&q=80",
            releaseYear = 1959,
            releaseDate = "July 22, 1959",
            rating = 6.8,
            voteCount = 9200,
            runtimeMinutes = 79,
            genres = listOf("Sci-Fi", "Horror", "Cult", "Classics"),
            resolutionBadge = "1080p Restored",
            language = "English",
            country = "United States",
            director = "Ed Wood",
            cast = listOf(
                CastMember("Bela Lugosi", "Ghoul Man", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200"),
                CastMember("Vampira", "Vampire Girl", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200"),
                CastMember("Tor Johnson", "Inspector Daniel Clay", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200")
            ),
            production = "Criswell Productions (Public Domain)",
            trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4",
            videoSources = listOf(
                VideoResolutionSource("Auto (Adaptive)", 1920, 1080, 3800000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"),
                VideoResolutionSource("1080p Full HD", 1920, 1080, 3800000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"),
                VideoResolutionSource("720p HD", 1280, 720, 1900000L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4")
            ),
            isFeatured = false
        )
    )

    val genres: List<String> = listOf(
        "All", "Sci-Fi", "Action", "Animation", "Fantasy", "Comedy", "Drama", "Horror", "Thriller", "Mystery", "Adventure", "Classics", "Romance"
    )
}
