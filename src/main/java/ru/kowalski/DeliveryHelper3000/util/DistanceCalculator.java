package ru.kowalski.DeliveryHelper3000.util;

import org.springframework.stereotype.Component;
import ru.kowalski.DeliveryHelper3000.model.Order;
import ru.kowalski.DeliveryHelper3000.model.Partner;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.*;

@Component
public class DistanceCalculator {

    private static final int EARTH_RADIUS_KM = 6371; // Радиус Земли в километрах
    private static final double START_POINT_LATITUDE = 46.3438095; // Широта стартовой точки
    private static final double START_POINT_LONGITUDE = 48.0376301; // Долгота стартовой точки
    private static final String START_POINT_NAME = "Склад"; // Название стартовой точки
    private static final String START_POINT_ADDRESS = "ул.Челюскинцев д.18"; // Адрес стартовой точки
    //TODO сделать изменяемым
    private static final int LOADING_TIME_PER_POINT_MINUTES = 15; // Время нахождения на одной торговой точке


    // Метод для составления маршрутного листа с помощью алгоритма "ближайшего соседа" с учетом времени доставки
    public List<Partner> createRouteWithTime(List<Order> orders, LocalDateTime timeOfDeliverySrarts) {

        // Устанавливаем склад в качестве стартовой точки
        Partner startingPoint = new Partner();
        startingPoint.setPartnerName(START_POINT_NAME);
        startingPoint.setPartnerAddress(START_POINT_ADDRESS);
        startingPoint.setLatitude(START_POINT_LATITUDE);
        startingPoint.setLongitude(START_POINT_LONGITUDE);

        // получаем из списка заказов список торговых точек
        Set<Partner> remainingPartners = new HashSet<>();
        for(Order order:orders){
            remainingPartners.add(order.getPartner());
        }
        List<Partner> route = new ArrayList<>();

        // Добавляем начальную точку (склад) в маршрут
        route.add(startingPoint);
        remainingPartners.remove(startingPoint);

        // Объявляем время выезда со склада
        int currentTime = convertLocalTimeToInt(timeOfDeliverySrarts.toLocalTime().truncatedTo(ChronoUnit.MINUTES));

        // Находим ближайшую точку для доставки на каждой итерации
        while (!remainingPartners.isEmpty()) {
            Partner currentPoint = route.get(route.size() - 1);
            Partner nearestNeighbor = findNearestNeighbor(currentPoint, remainingPartners);

            //TODO изменить способ получения (не обязательно)
            Order tempOrder = nearestNeighbor.getOrders().get(nearestNeighbor.getOrders().size()-1);

            // Рассчитываем время доставки для текущей точки
            int deliveryTimeMinutes;

            if (route.size() == 1) {
                // Если торговая точка первая в маршруте после склада, время доставки равно timeForDelivery
                deliveryTimeMinutes = nearestNeighbor.getTimeForDeliveryInMinutes();
            } else {
                // Учитываем время загрузки и разгрузки
                deliveryTimeMinutes = LOADING_TIME_PER_POINT_MINUTES;

                // Учитываем время прибытия на предыдущую точку (если маршрут содержит хотя бы одну предыдущую точку кроме склада)
                if (route.size() > 1) {
                    Partner previousPoint = route.get(route.size() - 2);
                    deliveryTimeMinutes += nearestNeighbor.getTimeForDeliveryInMinutes() - previousPoint.getTimeForDeliveryInMinutes();
                    if (nearestNeighbor.equals(startingPoint)){
                        deliveryTimeMinutes += previousPoint.getTimeForDeliveryInMinutes();
                    }
                }
            }

            int deliveryWindowStart = convertLocalTimeToInt(tempOrder.getDeliveryTimeWindowStart().toLocalTime());
            int deliveryWindowEnd = convertLocalTimeToInt(tempOrder.getDeliveryTimeWindowEnd().toLocalTime());

            // Проверяем, укладываемся ли во временные ограничения для этой точки
            if (currentTime + deliveryTimeMinutes >= deliveryWindowStart && currentTime + deliveryTimeMinutes <= deliveryWindowEnd) {
                // Если да, добавляем точку в маршрут
                route.add(nearestNeighbor);
                remainingPartners.remove(nearestNeighbor);

                // Обновляем текущее время
                currentTime += deliveryTimeMinutes;
            } else {
                // Если не укладываемся в ограничения, перемещаемся к следующей точке (ближайшей)
                remainingPartners.remove(nearestNeighbor);
            }
        }

        // Возвращаем маршрут с точкой возврата на склад
        route.add(startingPoint);
        return route;
    }

    // Метод для поиска ближайшей точки для доставки
    private Partner findNearestNeighbor(Partner currentPoint, Set<Partner> remainingPartners) {
        Partner nearestNeighbor = null;
        double minDistance = Double.MAX_VALUE;

        for (Partner partner : remainingPartners) {
            double distance = calculateDistance(currentPoint, partner);
            if (distance < minDistance) {
                nearestNeighbor = partner;
                minDistance = distance;
            }
        }

        return nearestNeighbor;
    }

    // Метод для расчета расстояния между двумя точками по формуле Гаверсинуса
    private double calculateDistance(Partner point1, Partner point2) {
        double lat1 = toRadians(point1.getLatitude());
        double lon1 = toRadians(point1.getLongitude());
        double lat2 = toRadians(point2.getLatitude());
        double lon2 = toRadians(point2.getLongitude());

        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        double a = sin(deltaLat / 2) * sin(deltaLat / 2)
                + cos(lat1) * cos(lat2) * sin(deltaLon / 2) * sin(deltaLon / 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));

        // Расстояние между точками в километрах
        double distance = EARTH_RADIUS_KM * c;
        return distance;
    }

    // Метод для конвертации из формата LocalTime в минуты (int)
    private int convertLocalTimeToInt(LocalTime time){
        int hours = time.getHour();
        int minutes = time.getMinute();
        return hours * 60 + minutes;
    }
}




