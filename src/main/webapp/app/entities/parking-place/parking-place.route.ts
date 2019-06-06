import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ParkingPlace } from 'app/shared/model/parking-place.model';
import { ParkingPlaceService } from './parking-place.service';
import { ParkingPlaceComponent } from './parking-place.component';
import { ParkingPlaceDetailComponent } from './parking-place-detail.component';
import { ParkingPlaceUpdateComponent } from './parking-place-update.component';
import { ParkingPlaceDeletePopupComponent } from './parking-place-delete-dialog.component';
import { IParkingPlace } from 'app/shared/model/parking-place.model';

@Injectable({ providedIn: 'root' })
export class ParkingPlaceResolve implements Resolve<IParkingPlace> {
    constructor(private service: ParkingPlaceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IParkingPlace> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<ParkingPlace>) => response.ok),
                map((parkingPlace: HttpResponse<ParkingPlace>) => parkingPlace.body)
            );
        }
        return of(new ParkingPlace());
    }
}

export const parkingPlaceRoute: Routes = [
    {
        path: '',
        component: ParkingPlaceComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'parkingApp.parkingPlace.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: ParkingPlaceDetailComponent,
        resolve: {
            parkingPlace: ParkingPlaceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'parkingApp.parkingPlace.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: ParkingPlaceUpdateComponent,
        resolve: {
            parkingPlace: ParkingPlaceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'parkingApp.parkingPlace.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: ParkingPlaceUpdateComponent,
        resolve: {
            parkingPlace: ParkingPlaceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'parkingApp.parkingPlace.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const parkingPlacePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ParkingPlaceDeletePopupComponent,
        resolve: {
            parkingPlace: ParkingPlaceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'parkingApp.parkingPlace.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
