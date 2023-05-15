package com.udacity.asteroidradar.utils

const val API_QUERY_DATE_FORMAT = "yyyy-MM-dd"
const val DEFAULT_END_DATE_DAYS = 7
const val BASE_URL = "https://api.nasa.gov/"

// Name of Notification Channel for verbose notifications of background work
@JvmField
val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence = "Verbose WorkManager Notifications"

@JvmField
val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"

const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1

const val WORK_SLEEP_DURATION = 3000L
