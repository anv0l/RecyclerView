package otus.gpb.recyclerview.data

import java.time.Instant
import java.time.temporal.ChronoUnit

private val users = mutableSetOf<User>() // all users on a platform

val friendList = mutableSetOf<User>()    // added as friends

private val chatDB: MutableList<Chat> = emptyList<Chat>().toMutableList()

/**
 * acts as database:
 * - generates all chats, sorts by time (init mock database)
 * - in main activity loads first page (slice of first pageSize records
 * - when scrolling, loads next page.
 */

private const val userCount = 100
private const val maxFriends = 50
private const val pageSize = 15
private val lag = (50..100).random()..(100..200).random()

private val userNames = setOf(
    "Meadow Burton",
    "Zander Rice",
    "Ada Marin",
    "Aldo Rogers",
    "Madelyn Wiggins",
    "Azariah Walters",
    "Samara Mosley",
    "Rayden Koch",
    "Milana James",
    "Jaxson Miller",
    "Isabella Duffy",
    "Kyng Frazier",
    "Octavia Nicholson",
    "Rodrigo Donaldson",
    "Natasha Morrow",
    "Kyree Taylor",
    "Sofia Marquez",
    "Malakai Mullen",
    "Shay Byrd",
    "Cristian Poole",
    "Bonnie Carrillo",
    "Wade Marin",
    "Waldo Dorsey",
    "Alejandro Miranda",
    "Kieth Rosales",
    "Diane Frazier",
    "Angelita Finley",
    "Brittany Becker",
    "Anastasia Lyons",
    "Johanna Jarvis",
    "Tara Delacruz",
    "Darron Jacobson",
    "Margarito Dennis",
    "Sheldon Sosa",
    "Nelda Hawkins",
    "Robt Fields",
    "Cornelia Scott",
    "Estella Mays",
    "George Gray",
    "Stanford Brooks",
    "Lacy Meyers",
    "Nicolas Shaffer",
    "Beau Goodwin",
    "Ira Mcgrath"
)

private val randomStatuses = setOf(
    "Twenty one year old guys are like car thiefs, they tend to get the easy ones.",
    "Why do people with bad breath always have to tell me a damn secrets???",
    "LIKE IF: Knowing you`re totally screwed for an exam, but staying on facebook and not studying.",
    "I need to be blessed with the abilty to start studying..... because studying is not a part of my life.... #IHateFinals!!!",
    "the awkward moment when you forget how to fly.....",
    "The awkward moment when you need to send that unfashionable friend to Mallzee.",
    "Who else used to think Courage the Cowardly dog was the scariest show ever?",
    "They keep saying the right person will come along; I think a truck hit mine.",
    "Everyone judges. So do whatever you want, the ones who love you will never leave.",
    "A girl`s smile hides thousand words, a girl`s tears hides thousand feelings.",
    "Getting a text from that special someone right when you`ve felt like they forgot about you.",
    "This looks perfect",
    "I swear, school wouldn`t be half as bad if we didn`t have to wake up so damn early.",
    "The only people who truly know your story are the ones that helped you write it.",
    "Age does not protect you from love, but love to some extent protects you from age.",
    "When I hear myself eating crunchy food, I wonder if other people can hear it too.",
    "Like if: Saying random numbers when someone is counting to make them lose count",
    "One of these days common sense will come back in fashion.",
    "Some say long distance relationships never succeed, I say with enough effort, time and commitment, love will find its way.",
    "I’m constantly torn between the ‘be kind to everyone’ and the ‘fuck everyone you owe them nothing’ mentalities.",
    "LIKE IF: I look at my watch, and when someone asks the time, I have to look again.",
    "Letters begin with ABC. Numbers begin with 123. Music begins with Do Re Mi. Love begins with you and me.",
    "Ever since I met you, it hasn`t been the same. All you got me doing is drawing hearts around your name.",
    "Like if you ever fell down the stairs thinking you were walking down a slope.",
    "Cellphone, laptop, iPod, TV = My best friends.",
    "Facebook is like a refrigerator. You get bored and keep checking, but nothing ever changes.",
    "Dear eyelashes, wishbones, pennies, shooting stars, 11:11 and birthday candles. What happened to my wishes? Sincerely, still waiting.",
    "WHEN YOU TYPE LIKE THIS IT MAKES ME THINK YOU`RE YELLING, when u type like dis it makes me think u a lil kidd, WhEn YoU TYpE LIkE ThIs It MAkEs ME ThInk YOUr MEntAllY HAndY CAppEd",
    "Dear everyone, always remember that when you fall, I`ll pick you up. But just AFTER I finish laughing.",
    "do we have time for me? no no no no no no no no no no no no",
    "I always pretend to care about teachers personal life, to waste time in their class.",
    "LIKE IF: Telling your mom something funny & ending up getting yelled at.",
    "I have to plug my mobile phone into the charger so much throughout the day, that I basically have a landline again.",
    "Having problems are inevitable, but being stressed out because of those problems is an option.",
    "If Monday were shoes, they`d be Crocs.",
    "Accidentally erase something you just typed on your iPhone? To undo that, just shake it!",
    "I`m sorta, kinda, maybe, slightly, just a little tiny bit, addicted to you.",
    "Oooooh, thats a bit too harsh. Let me put a `lol` at the end of it.",
    "Dear Santa, this year, please send clothes for all those poor ladies in Daddy`s computer.",
    "they say best friends are hard to find: well that`s cause the best ones are already mine...:)",
)


private fun generateUsers() {
    for (i in 1..userCount) {
        users.add(
            User(
                id = if (users.isEmpty()) 1 else users.maxOf { it.id } + 1,
                name = (userNames).random(),
                avatar = "https://i.pravatar.cc/150?img=${(1..70).random()}",
                lastSeen = getRandomTime(),
                isVerified = (0..10).random() < 2,
                statusMessage = randomStatuses.randomOrNull(),
                nickName = ""
            )
        )
    }
}

private fun befriendRandomUsers() {
    for (i in 1..maxFriends) {
        friendList.add((users - friendList).random())
    }
}

private fun generateChats() {
    repeat(userCount) {
        val chat = generateSingleChat()
        chatDB.add(chat)
    }
    chatDB
}

private fun generateSingleChat(): Chat {
    // can be from the friend list as well as random unknown contacts
    val fromFriendList = (0..10).random() < 7
    val chat = Chat(
        user = if (fromFriendList) friendList.random() else (users - friendList).random(),
        timeRead = getRandomTime(),
        isScam = if (fromFriendList) false else ((0..3).random() < 1),
        mutedUntil = null,
        isMutedForever = (0..10).random() < 4,
        isArchived = false,
        isVoip = (0..10).random() < 2,
        id = chatDB.maxOfOrNull { it.id }?.plus(1) ?: 1,
        directionLast = if ((0..3).random() < 2) MessageDirection.IN else MessageDirection.OUT,
        statusLast = if ((0..3).random() < 2) MessageStatus.READ else MessageStatus.DELIVERED,
        textLast = randomStatuses.random(),
        isMentionedLast = (0..10).random() < 2,
        unreadCount = if ((0..3).random() < 3) 0 else (0..10).random(),
        timeLast = getRandomTime(start = Instant.now().minus(666, ChronoUnit.DAYS))
    )
    chat.unreadCount = (0..10).random()
    return chat
}

fun initDB() {
    generateUsers()
    befriendRandomUsers()
    generateChats()
    chatDB.sortByDescending { it.timeLast }
}

private fun getRandomTime(
    start: Instant = Instant.now().minus(365, ChronoUnit.DAYS),
    end: Instant = Instant.now()
): Instant {
    val startSeconds = start.epochSecond
    val endSeconds = end.epochSecond
    val randomSeconds = (startSeconds until endSeconds).random()
    return Instant.ofEpochSecond(randomSeconds)
}

fun loadMoreChatsFromDB(page: Int): List<Chat> {
    return if ((page + 1) * pageSize > userCount) emptyList()
    else {
        Thread.sleep((lag).random().toLong()) // random delay as if getting data over the Internet
        chatDB.slice(page * pageSize until (page + 1) * pageSize)
    }
}

fun getChatsCount(): Int {
    return chatDB.size
}