package com.example.androidchallenge.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchallenge.R
import com.example.androidchallenge.common.ICON_BASE_URI
import com.example.androidchallenge.common.ICON_SIZE_DEFAULT
import com.example.androidchallenge.common.ICON_TYPE_PNG
import com.example.androidchallenge.common.Util
import com.example.androidchallenge.databinding.ItemWeatherForecastBinding
import com.example.androidchallenge.network.models.Daily
import com.squareup.picasso.Picasso
import java.lang.StringBuilder

class WeatherForecastAdapter: RecyclerView.Adapter<WeatherForecastVH>() {

    private val dailyList = mutableListOf<Daily>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherForecastVH {
        val binding = ItemWeatherForecastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return WeatherForecastVH(binding)
    }

    override fun onBindViewHolder(holder: WeatherForecastVH, position: Int) {
        holder.bind(dailyList[position])
    }

    override fun getItemCount(): Int {
        return dailyList.size
    }

    fun updateList(newList: List<Daily>) {
        dailyList.clear()
        dailyList.addAll(newList)
    }
}

class WeatherForecastVH(val binding: ItemWeatherForecastBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(daily: Daily) {

        val (weather, icon) = if (daily.weather.isNotEmpty()) {
            daily.weather[0].run {
                "$main - $description"
            } to daily.weather[0].icon
        } else {
            "" to ""
        }

        binding.tvWeatherForecastDayDate.text = Util.getDayDateFromSeconds(daily.dt)
        binding.tvWeatherForecastWeather.text = weather
        binding.tvWeatherForecastPressure.text = itemView.context.getString(R.string.weather_forecast_pressure, daily.pressure)
        binding.tvWeatherForecastWindSpeed.text = itemView.context.getString(R.string.weather_forecast_wind_speed, daily.windSpeed)
        binding.tvWeatherForecastWindDirection.text = itemView.context.getString(R.string.weather_forecast_wind_direction, daily.windDeg)
        binding.tvWeatherForecastHumidity.text = itemView.context.getString(R.string.weather_forecast_humidity, daily.humidity)

        val url = StringBuilder()
            .append(ICON_BASE_URI)
            .append(icon)
            .append(ICON_SIZE_DEFAULT)
            .append(ICON_TYPE_PNG).toString()
        Picasso.with(itemView.context)
            .load(url)
            .into(binding.ivWeatherForecastIcon)

        daily.temp.let {
            binding.tvWeatherForecastMorning.text = itemView.context.getString(R.string.weather_forecast_temp_mor, it.morn)
            binding.tvWeatherForecastDay.text = itemView.context.getString(R.string.weather_forecast_temp_day, it.day)
            binding.tvWeatherForecastEvening.text = itemView.context.getString(R.string.weather_forecast_temp_eve, it.eve)
            binding.tvWeatherForecastNight.text = itemView.context.getString(R.string.weather_forecast_temp_nig, it.night)
            binding.tvWeatherForecastMin.text = itemView.context.getString(R.string.weather_forecast_temp_min, it.min)
            binding.tvWeatherForecastMax.text = itemView.context.getString(R.string.weather_forecast_temp_max, it.max)
        }
    }
}
