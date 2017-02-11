# Forward Checking

## Implementation:
- Maintain a list of `Coordinate` objects where a bulb may be placed
	+ Upon placement of a bulb, remove all coordinates that would violate a constraint
	+ Pull from this list when looking for a new bulb to place
		+ For random, shuffle before pulling from list (or use random index)
