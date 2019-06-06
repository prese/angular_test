import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IParkingPlace } from 'app/shared/model/parking-place.model';
import { AccountService } from 'app/core';
import { ParkingPlaceService } from './parking-place.service';

@Component({
    selector: 'jhi-parking-place',
    templateUrl: './parking-place.component.html'
})
export class ParkingPlaceComponent implements OnInit, OnDestroy {
    parkingPlaces: IParkingPlace[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected parkingPlaceService: ParkingPlaceService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.parkingPlaceService
            .query()
            .pipe(
                filter((res: HttpResponse<IParkingPlace[]>) => res.ok),
                map((res: HttpResponse<IParkingPlace[]>) => res.body)
            )
            .subscribe(
                (res: IParkingPlace[]) => {
                    this.parkingPlaces = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInParkingPlaces();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IParkingPlace) {
        return item.id;
    }

    registerChangeInParkingPlaces() {
        this.eventSubscriber = this.eventManager.subscribe('parkingPlaceListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
