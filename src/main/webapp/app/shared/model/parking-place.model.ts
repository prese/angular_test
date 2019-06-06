import { Moment } from 'moment';

export interface IParkingPlace {
    id?: number;
    dateIn?: Moment;
    dateOut?: Moment;
    userLogin?: string;
    userId?: number;
}

export class ParkingPlace implements IParkingPlace {
    constructor(public id?: number, public dateIn?: Moment, public dateOut?: Moment, public userLogin?: string, public userId?: number) {}
}
