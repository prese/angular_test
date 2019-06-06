import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IParkingPlace } from 'app/shared/model/parking-place.model';

type EntityResponseType = HttpResponse<IParkingPlace>;
type EntityArrayResponseType = HttpResponse<IParkingPlace[]>;

@Injectable({ providedIn: 'root' })
export class ParkingPlaceService {
    public resourceUrl = SERVER_API_URL + 'api/parking-places';

    constructor(protected http: HttpClient) {}

    create(parkingPlace: IParkingPlace): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(parkingPlace);
        return this.http
            .post<IParkingPlace>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(parkingPlace: IParkingPlace): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(parkingPlace);
        return this.http
            .put<IParkingPlace>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IParkingPlace>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IParkingPlace[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(parkingPlace: IParkingPlace): IParkingPlace {
        const copy: IParkingPlace = Object.assign({}, parkingPlace, {
            dateIn: parkingPlace.dateIn != null && parkingPlace.dateIn.isValid() ? parkingPlace.dateIn.toJSON() : null,
            dateOut: parkingPlace.dateOut != null && parkingPlace.dateOut.isValid() ? parkingPlace.dateOut.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dateIn = res.body.dateIn != null ? moment(res.body.dateIn) : null;
            res.body.dateOut = res.body.dateOut != null ? moment(res.body.dateOut) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((parkingPlace: IParkingPlace) => {
                parkingPlace.dateIn = parkingPlace.dateIn != null ? moment(parkingPlace.dateIn) : null;
                parkingPlace.dateOut = parkingPlace.dateOut != null ? moment(parkingPlace.dateOut) : null;
            });
        }
        return res;
    }
}
