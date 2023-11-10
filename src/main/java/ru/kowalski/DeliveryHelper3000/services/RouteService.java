package ru.kowalski.DeliveryHelper3000.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kowalski.DeliveryHelper3000.model.Car;
import ru.kowalski.DeliveryHelper3000.model.Route;
import ru.kowalski.DeliveryHelper3000.repository.CarRepository;
import ru.kowalski.DeliveryHelper3000.repository.RouteRepository;
import ru.kowalski.DeliveryHelper3000.util.CarNotFoundException;
import ru.kowalski.DeliveryHelper3000.util.RouteNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    public void createNewRoute(Route route){
        routeRepository.save(route);
    }

    public void updateRouteById(Long routeId){
        Route updatedRoute = routeRepository.findById(routeId).orElseThrow(RouteNotFoundException::new);
        routeRepository.save(updatedRoute);
    }

    public void deleteRouteById(Long routeId){
        Route route = routeRepository.findById(routeId).orElseThrow(RouteNotFoundException::new);
        routeRepository.deleteById(route.getRouteId());
    }

    public Route findRouteById(Long routeId) {
        return routeRepository.findById(routeId).orElseThrow(RouteNotFoundException::new);
    }

    public List<Route> findAllRoutes(){
        return routeRepository.findAll();
    }

}
