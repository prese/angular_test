import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IParkingPlace } from 'app/shared/model/parking-place.model';
import { ParkingPlaceService } from './parking-place.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-parking-place-update',
    templateUrl: './parking-place-update.component.html'
})
export class ParkingPlaceUpdateComponent implements OnInit {
    parkingPlace: IParkingPlace;
    isSaving: boolean;

    users: IUser[];
    dateIn: string;
    dateOut: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected parkingPlaceService: ParkingPlaceService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ parkingPlace }) => {
            this.parkingPlace = parkingPlace;
            this.dateIn = this.parkingPlace.dateIn != null ? this.parkingPlace.dateIn.format(DATE_TIME_FORMAT) : null;
            this.dateOut = this.parkingPlace.dateOut != null ? this.parkingPlace.dateOut.format(DATE_TIME_FORMAT) : null;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.parkingPlace.dateIn = this.dateIn != null ? moment(this.dateIn, DATE_TIME_FORMAT) : null;
        this.parkingPlace.dateOut = this.dateOut != null ? moment(this.dateOut, DATE_TIME_FORMAT) : null;
        if (this.parkingPlace.id !== undefined) {
            this.subscribeToSaveResponse(this.parkingPlaceService.update(this.parkingPlace));
        } else {
            this.subscribeToSaveResponse(this.parkingPlaceService.create(this.parkingPlace));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IParkingPlace>>) {
        result.subscribe((res: HttpResponse<IParkingPlace>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
